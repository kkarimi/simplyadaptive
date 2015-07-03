import numpy as np
#from sklearn import metrics
import csv
thefile = csv.reader(open("auc.txt", "rb"), 
                    delimiter=',', quoting=csv.QUOTE_NONE)


#f = open("auc.txt")
#y = np.array([1, 1, 2, 2])


#pos_label = Label considered as positive and others are considered negative.
predictions = thefile



#fpr, tpr, thresholds = metrics.roc_curve(y, predictions, pos_label=2)
metrics.auc(fpr, tpr)

fpr, tpr, thresholds = metrics.roc_curve(true_labels, predictions, pos_label=1)
auc = metrics.auc(fpr,tpr)



