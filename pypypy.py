import numpy as np
from sklearn import metrics
from sklearn import linear_model
from sklearn.metrics import auc_score

testfile = open('auc.txt')
#ignore the test header
testfile.next()

X_test_A = []
X_test_B = []
for line in testfile:
    splitted = line.rstrip().split(',')
    A_features = [float(item) for item in splitted[0:1]]
    B_features = [float(item) for item in splitted[1:]]
    X_test_A.append(A_features)
    X_test_B.append(B_features)
testfile.close()

X_test_A = np.array(X_test_A)
X_test_B = np.array(X_test_B)


fpr, tpr, thresholds = metrics.roc_curve(X_test_A,X_test_B, pos_label=1)
auc = metrics.auc(fpr,tpr)
print auc


