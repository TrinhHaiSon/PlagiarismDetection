# -*- coding: utf-8 -*-
import math
from .tfidf import top
from .config import *
from .keyword_extraction import Doc
import urllib.request
import re
import os
import requests

class CandidateSearch():
    @staticmethod
    def _search(n, list_keyword):
        # t = time.time()
        url = 'http://202.191.57.85:8080/APISearchEs/SearchLimited/SearchESLimitFile?numberFile=' + str(n) + '&keyword='
        for keyword_ in list_keyword:
            keyword = re.sub('_',' ',keyword_)
            url = url + keyword + '+'
        url = url[:-1]
        r = requests.get(url)
        return r.json()["hits"]["hits"]

    @staticmethod
    def search(num_file, text, log_file=None):
        doc = Doc(text)
        list_keyword = doc.getKeyWord()
        result = CandidateSearch._search(num_file, list_keyword)
        if log_file is not None:
            with open(log_file, 'w+', encoding='utf-8') as file:
                file.write(str(list_keyword))
                file.write('\n\n')
                file.write(json.dumps(result))
        return result

import json
import sys
if __name__ == "__main__":
    num_file = sys.argv[1]
    filepath = sys.argv[2]
    log_file = None
    try:
        log_file = sys.argv[3]
    except:
        pass
    text = open(filepath, "r", encoding="utf-8").read()
    print(json.dumps(CandidateSearch.search(num_file, text, log_file)))

"""
python -m candidate_search.run 12 "E:/1.txt"
"""