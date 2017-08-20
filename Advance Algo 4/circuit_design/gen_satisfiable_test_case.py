from __future__ import division, print_function
import getopt
import os
import random
import subprocess
import sys

def print_usage():
  print('Generates a random SATISFIABLE test case for the \'Integrated Circuit Design\'')
  print('problem from Week 4 of the Coursera Advanced Algorithms and Complexity MOOC:')
  print('https://www.coursera.org/learn/advanced-algorithms-and-complexity')
  print()
  print('The test case is generated incrementally in a certain number of rounds.')
  print('You can monitor the progress by watching the standard error output. Every time')
  print('an attempt is made to add clauses to the working formula (whose satisfiability')
  print('is then tested with the new clauses), a dot \'.\' is printed to stderr.')
  print()
  print('Usage: %s [OPTIONS] NUM_VARS NUM_CLAUSES' % sys.argv[0])
  print('Options:')
  print('  --start-with=FILE    Starts the test case generation with the clauses in FILE.')
  print('                       It is assumed that FILE specifies a set of SATISFIABLE')
  print('                       clauses.')
  print('  --output=FILE        File where the test case is written. If this option is')
  print('                       not specified, then the test case is written to stdout.')

opts, args = getopt.getopt(sys.argv[1:], 'h', ['help', 'start-with=', 'output='])

for o, _ in opts:
  if o == '-h' or o == '--help':
    print_usage()
    sys.exit(0)

if len(args) == 0:
  print_usage()
  sys.exit(0)
elif len(args) != 2:
  print('Error: Missing NUM_VARS and/or NUM_CLAUSES.', file = sys.stderr)
  sys.exit(1)

try:
  num_vars = int(args[0])
  if num_vars <= 0:
    raise ValueError()
except ValueError:
  print('Error: NUM_VARS must be an integer greater than 0', file = sys.stderr)
  sys.exit(1)
try:
  num_clauses = int(args[1])
  if num_clauses <= 0:
    raise ValueError()
except ValueError:
  print('Error: NUM_CLAUSES must be an integer greater than 0', file = sys.stderr)
  sys.exit(1)

clauses = set()
negated_clauses = set()

out_file = sys.stdout

for o, a in opts:
  if o == '--start-with':
    rejected_clauses = set()
    with open(a) as f:
      first_line = True
      for line in f:
        if first_line:
          first_line = False
          continue
        if len(clauses) > num_clauses:
          break
        parts = line.split()
        if len(parts) != 2:
          continue
        try:
          a = int(parts[0])
          b = int(parts[1])
        except ValueError:
          continue
        if a == 0 or b == 0 or -a == b:
          continue
        clause = (min(a, b), max(a, b),)
        if ((a < -num_vars or num_vars < a) or
            (b < -num_vars or num_vars < b)):
          rejected_clauses.add(clause)
          continue
        clauses.add(clause)
        negated_clauses.add((min(-a, -b), max(-a, -b),))
    print('Starting with %d clause(s). Rejected %d clause(s):' % (len(clauses), len(rejected_clauses),), *rejected_clauses, file = sys.stderr)
  elif o == '--output':
    out_file = open(a, 'w+')

clauses_str = '\n'.join([str(clause[0]) + ' ' + str(clause[1]) for clause in clauses])

clauses_per_round = [min(max(10, num_clauses // 20), num_clauses)]
clauses_last_round = clauses_per_round[0]
total = clauses_last_round
while total < num_clauses:
  clauses_this_round = min(max(10, num_clauses // 40000, clauses_last_round * 8 // 10), num_clauses - total)
  clauses_per_round.append(clauses_this_round)
  total += clauses_this_round
  clauses_last_round = clauses_this_round

print('Clauses per round:', *clauses_per_round, file = sys.stderr)
print('Num rounds: %d' % len(clauses_per_round), file = sys.stderr)

total = 0
for start_r in range(len(clauses_per_round)):
  if total >= len(clauses):
    break
  total += clauses_per_round[start_r]

print(num_vars, num_clauses, file = out_file)
# http://stackoverflow.com/questions/9573244/most-elegant-way-to-check-if-the-string-is-empty-in-python
if clauses_str:
  print(clauses_str, file = out_file)
# flush() and fsync() `out_file'
# http://stackoverflow.com/questions/7127075/what-exactly-the-pythons-file-flush-is-doing
out_file.flush()
os.fsync(out_file.fileno())

for r in range(start_r, len(clauses_per_round)):
  clauses_this_round = clauses_per_round[r]
  print('Round %d (%d clause(s))' % (r+1, clauses_this_round,), file = sys.stderr)
  while True:
    print('.', end = '', file = sys.stderr)

    new_clauses = set()
    negated_new_clauses = set()
    for i in range(clauses_this_round):
      while True:
        a = random.randint(1, num_vars * 2)
        if a > num_vars:
          a -= 2 * num_vars + 1
        while True:
          b = random.randint(1, num_vars * 2)
          if b > num_vars:
            b -= 2 * num_vars + 1
          if a != b and -a != b:
            break
        new_clause = (min(a, b), max(a, b),)
        if ((new_clause not in clauses and new_clause not in new_clauses) and
            (new_clause not in negated_clauses and new_clause not in negated_new_clauses)):
          break
      new_clauses.add(new_clause)
      negated_new_clauses.add((min(-a, -b), max(-a, -b),))

    new_clauses_str = '\n'.join([str(new_clause[0]) + ' ' + str(new_clause[1]) for new_clause in new_clauses])

    proc = subprocess.Popen(['java', 'CircuitDesign'], stdin = subprocess.PIPE, stdout = subprocess.PIPE, universal_newlines = True)
    assert len(new_clauses) == clauses_this_round
    print(num_vars, len(clauses) + clauses_this_round, file = proc.stdin)
    if clauses_str:
      print(clauses_str, file = proc.stdin)
    print(new_clauses_str, file = proc.stdin)
    stdoutdata, _ = proc.communicate()
    assert proc.returncode == 0

    stdout_lines = stdoutdata.splitlines()
    if stdout_lines[0] == 'SATISFIABLE':
      print(new_clauses_str, file = out_file)
      out_file.flush()
      os.fsync(out_file.fileno())

      print(file = sys.stderr)
      clauses.update(new_clauses)
      negated_clauses.update(negated_new_clauses)
      if not clauses_str:
        clauses_str = new_clauses_str
      else:
        clauses_str += '\n' + new_clauses_str
      break
    else:
      sys.stderr.flush() # make sure that the dot is visible
