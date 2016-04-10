import random
import sys

input_file = sys.argv[1]
output_file = sys.argv[2]

with open(input_file, 'r') as fin:
  data = [line for line in fin]
random.shuffle(data)
with open(output_file, 'w') as fout:
  for line in data:
    fout.write(line)
