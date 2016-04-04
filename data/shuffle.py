import random
with open('random1', 'r') as fin:
  data = [line for line in fin]
random.shuffle(data)
with open('random5', 'w') as fout:
  for line in data:
    fout.write(line)
