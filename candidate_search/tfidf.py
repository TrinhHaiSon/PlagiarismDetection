# -*- coding: utf-8 -*-
import math
import re
import os
import pdb
file_dir = os.path.dirname(os.path.realpath(__file__))

def tf(text):
    dict = {}
    list_word = text.split()
    for word in list_word:
        if word in dict:
            dict[word] += 1.0
        else:
            dict[word] = 1.0
    try:
        maxtf = max(dict.values())
    except:
        maxtf = 1
    for key in dict.keys():
        dict[key] = dict[key]/maxtf
    return dict

def tfidf(text):
    dict = {}
    with open(file_dir + '/idf.txt', 'r', encoding='utf-8') as openfileobject:
        for line in openfileobject:
            l = line.split('\t')
            dict[l[0]] = float(l[1])
    t = tf(text)
    tf_idf = {}
    for word in t:
        try:
            d = dict[word]
        except:
            d = 1
        D = 340.0
        tf_idf[word] = t[word] * math.log(D / d)
    return tf_idf

def top(n,dict):
    list_key = list(dict.keys())
    list_value = []
    dict1 = {}
    for key in list_key:
        list_value.append(dict[key])
    for i in range(int(n)):
        if len(list_value) == 0:
            break
        a =  max(list_value)
        index = list_value.index(a)
        list_value.pop(index)
        k = list_key.pop(index)
        if re.match(u'[0-9A-Za-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễýĂÂÁẮẤẢẲẨÀẰẦẠẶẬÃẴẪĐÉÈẸẺẼÍÌỊỈĨÓÒỌỎÕỚỜƠỢỞỠÔỒỐỔỖỘỤỦŨÙÚƯỨỪỮỬỰÊẾỀỆỂỄÝ_]+',k):
            if re.match(u'^[0-9]+$',k):
                i = i - 1
                continue
            dict1[k] = a
    return dict1

