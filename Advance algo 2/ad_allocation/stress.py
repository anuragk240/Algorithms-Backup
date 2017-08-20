from __future__ import print_function
import numpy as np
import subprocess

test_cases = ['11', '12', '52', '55', '65']
for t in test_cases:
  testcase_filename = 'tests/' + t
  print('Checking ' + testcase_filename)
  with open(testcase_filename, 'r') as f:
    testcase_lines = f.readlines()

  n, m = list(map(int, testcase_lines[0].split()))
  A = [list(map(int, testcase_lines[1 + i].split())) for i in range(n)]
  b = list(map(int, testcase_lines[1 + n].split()))
  c = list(map(int, testcase_lines[1 + n + 1].split()))

  proc = subprocess.Popen(['java', 'AdAllocation'], stdin = subprocess.PIPE, stdout = subprocess.PIPE, universal_newlines = True)
  print(n, m, file = proc.stdin)
  for i in range(n):
    print(*(A[i][j] for j in range(m)), file = proc.stdin)
  print(*b, file = proc.stdin)
  print(*c, file = proc.stdin)
  stdoutdata, _ = proc.communicate()
  if proc.returncode != 0:
    print('FAIL - the program did not return code 0.')
    continue

  actual_lines = stdoutdata.splitlines()

  with open(testcase_filename + '.a', 'r') as f:
    expected_lines = f.read().splitlines()

  if expected_lines[0] == 'No solution' or expected_lines[0] == 'Infinity':
    if len(actual_lines) != 1 or actual_lines[0] != expected_lines[0]:
      print('FAIL - the correct answer is \'%s\'' % expected_lines[0])
  else:
    if len(actual_lines) == 0 or actual_lines[0] != expected_lines[0]:
      print('FAIL - the correct answer is \'Bounded solution\'')
      continue
    if len(actual_lines) != 2:
      print('FAIL - expecting exactly 2 lines of output.')
      continue

    actual_x = list(map(float, actual_lines[1].split()))
    if len(actual_x) != m:
      print('FAIL - the solution vector should consist of %d numbers.' % m)
      continue
    expected_x = list(map(float, expected_lines[1].split()))
    assert len(expected_x) == m

    # First check all inequalities:
    prod = np.dot(A, actual_x)
    all_okay = True
    for i in range(n):
      if prod[i] > b[i] + 1e-3:
        print('FAIL - constraint %d is violated by %f.' % (i + 1, prod[i] - b[i],))
        all_okay = False
    if not all_okay:
      continue

    # Now check that the optimized objective value is not more than 1e-3 different from the correct optimal value.
    actual_maximum = np.dot(c, actual_x)
    expected_maximum = np.dot(c, expected_x)
    if abs(actual_maximum - expected_maximum) > 1e-3:
      print('FAIL - expected optimal value of %f differs more than 1e-3 from the found optimal value of %f.' % (expected_maximum, actual_maximum,))
      continue