from __future__ import print_function
import numpy as np
from scipy.optimize import linprog
import subprocess

for _ in range(100000):
  n, m = np.random.randint(1, 8, size = (2,))

  A = np.random.randint(-100, 100, size = (n, m,))
  b = np.random.randint(-1000000, 1000000, size = (n,))
  c = np.random.randint(-100, 100, size = (m,))

  print(n, m)
  for i in range(n):
    print(*(A[i][j] for j in range(m)))
  print(*b)
  print(*c)
  print()

  proc = subprocess.Popen(['java', 'Diet'], stdin = subprocess.PIPE, stdout = subprocess.PIPE, universal_newlines = True)
  print(n, m, file = proc.stdin)
  for i in range(n):
    print(*(A[i][j] for j in range(m)), file = proc.stdin)
  print(*b, file = proc.stdin)
  print(*c, file = proc.stdin)
  stdoutdata, _ = proc.communicate()
  assert proc.returncode == 0

  stdout_lines = stdoutdata.splitlines()

  print(stdoutdata, end = '')

  # Try to reduce "false positive" results, where linprog() returns an answer that is
  # not correct. Use the idea for "a slightly modified procedure" described at:
  # https://www.coursera.org/learn/advanced-algorithms-and-complexity/discussions/all/threads/XBz2qmB5EeaqYRKO7-Ax0Q/replies/GdzNnHYvEealrxI7520HyQ/comments/AAAGGXbSEeaF8w5uWHT1BQ
  linprog_res = linprog(-c, A_ub = A, b_ub = b, options = { 'tol': 1e-4 })
  if linprog_res.status != 2:
    prev_linprog_res = linprog_res
    tolerances = [1e-5, 1e-6, 1e-7, 1e-8, 1e-9, 1e-10, 1e-11, 1e-12, 1e-13]
    for tol in tolerances:
      linprog_res = linprog(-c, A_ub = A, b_ub = b, options = { 'tol': tol })
      if linprog_res.status == 2:
        linprog_res = prev_linprog_res
        break
      prev_linprog_res = linprog_res

  if linprog_res.status == 3:
    assert stdout_lines[0] == 'Infinity'
  elif linprog_res.status == 2:
    assert stdout_lines[0] == 'No solution'
  elif linprog_res.status == 0:
    x_ref = linprog_res.x
    print('x_ref =', ' '.join(list(map(lambda x: '%.18f' % float(x), x_ref))))
    assert stdout_lines[0] == 'Bounded solution'
    x_myprog = np.array([float(num_str) for num_str in stdout_lines[1].split(' ')])
    assert len(x_myprog) == len(x_ref)

    # Verify that all inequalities are satisfied.
    assert (np.dot(A, x_myprog) <= b + 1e-3).all()

    max_myprog = np.dot(c, x_myprog)
    print('max_myprog =', max_myprog)
    max_ref = np.dot(c, x_ref)
    print('max_ref =', max_ref)

    # Total pleasure differs from the optimum by at most 1e-3.
    assert (abs(max_myprog - max_ref) <= 1e-3)

  print()
