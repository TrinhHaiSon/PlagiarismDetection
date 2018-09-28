# -*- coding: utf-8 -*-
from .tfidf import *
from .pre_processing import *
import re
from .config import *
# import textract
from pyvi import ViTokenizer
import os


def extendPharse(word, text, doc, tm):
    # word = word.decode('utf-8')
    listPharseExtended = []
    listPharse = []
    # tm = top(100, tfidfDic).keys()
    s1 = u'( |\n|^)[a-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễý_0-9]+ ' + word + u'( |\n|^)'
    s2 = u'( |\n|^)' + word + u' [a-zăâáắấảẳẩàằầạặậãẵẫđéèẹẻẽíìịỉĩóòọỏõớờơợởỡôồốổỗộụủũùúưứừữửựêếềệểễý_0-9]+( |\n|$)'
    for m in re.finditer(s1, text):
        a = m.group()
        wordAdded = a.split()[0]
        if (wordAdded not in stw) and (wordAdded in tm) and (a not in listPharse):
            # print a, '|', wordAdded
            listPharse.append(a)
    for m in re.finditer(s2, text):
        a = m.group()
        wordAdded = a.split()[-1]
        if (wordAdded not in stw) and (wordAdded in tm) and (a not in listPharse):
            # print a, '|', wordAdded
            listPharse.append(a)

    # print listPharse
    for key in listPharse:
        key = re.sub(' +$|\n+', '', key)
        key = re.sub('^ +', '', key)
        a = re.sub(u'_| ', u' ', key)
        n = len(re.findall(a, doc.lower()))
        if n >= nrepeat and key not in listPharseExtended:
            listPharseExtended.append([key, n])
    return listPharseExtended


def generatePharse(word, text, doc, tfidfDict):
    global pharse
    a = extendPharse(word, text, doc, tfidfDict)
    if len(a) == 0:
        pharse.append(word)
    for key in a:
        if key[0] not in pharse:
            # print key[0], key[1]
            generatePharse(key[0], text, doc, tfidfDict)


class Doc():
    def __init__(self, text):
        self.text = nomalize(text)

    # def getText(self):
    #     try:
    #         text = textract.process(self.file)
    #         print(text)
    #     except Exception as err:
    #         print(err)
    #         return u''
    #     return nomalize(text)

    def getContent(self):
        text = self.text
        if len(text) == 0:
            return u''
        try:
            content = split(nomalize(getAbstract(text))) + getContent(text)
            if len(content) == 0:
                content = split(text)
        except:
            content = split(text)
        return content

    def getKeyWord(self):
        doc = self.text
        content = u''
        for i in self.getContent().lower().split('\n'):
            t = ViTokenizer.tokenize(i)
            content = content + t + u'\n'
        tfidfDict = tfidf(content)
        listkey_init = top(10, tfidfDict)
        tm = list(top(len(list(tfidfDict.keys())) * 4 / 5, tfidfDict).keys())
        global pharse
        pharse = []
        for word in listkey_init:
            generatePharse(word, content, doc, tm)
        return pharse
