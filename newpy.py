import csv
import numpy as np
from sklearn import metrics

with open('auc.txt', 'rb') as f:
   predictions = csv.reader(f)
   data = []
   for row in predictions:
	#print row
	data.append(row)
	

y = data[:,0]
x = data[:,1]
print x



fpr, tpr, thresholds = metrics.roc_curve(x,y, pos_label=1)
auc = metrics.auc(fpr,tpr)



