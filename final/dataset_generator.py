import numpy as np
import random
import csv

if __name__ == "__main__":
    M = np.array([[1, 2], [3, 4]])
    with open('data.csv', 'w') as f1:
        writer = csv.writer(f1, delimiter='\t', lineterminator='\n')
        for i in range(10000):
            x = np.array([random.random(), random.random()])
            b = np.array([random.gauss(0, 1), random.gauss(0, 1)])
            y = np.matmul(M, x.transpose()) + b.transpose()
            y = y.transpose()
            writer.writerow([x, y])