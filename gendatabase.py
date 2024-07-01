import requests
from lxml import etree
import os
import sqlite3
import time

db = "Test.db"
key = "../ObfuscationSort/trial/result/result.txt"
headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 Edg/117.0.2045.43',
    'Cache-Control': 'no-cache',
    'Cookie': '_ga=GA1.1.1961353754.1695382880; cf_clearance=lac7n76iwx38uX.kzLkVvUg8MH_HgOQQN6MJaOnNZLc-1695883975-0-1-46636906.f22b0957.70d8c1eb-0.2.1695883975; cf_chl_2=9f1b2435ca480a9; __cf_bm=2QFEtEKjNU26ANr0cdiCtsS2.zNBIt7EPM4HIG0Fjs8-1695884736-0-AZ4+5o65J/cmjP5Ef58PBQLNDZRNJp3spDUSGcb42vzIGBELKuekimsX0+2hE36jwBHlcxBewiMhDUo7PhIHczI=; cf_chl_rc_i=1; MVN_SESSION=eyJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7InVpZCI6ImYxZmFmNjUxLTU5M2MtMTFlZS1hNjQzLTliNDlmY2I0N2YxYyJ9LCJleHAiOjE3Mjc0MjEzNjMsIm5iZiI6MTY5NTg4NTM2MywiaWF0IjoxNjk1ODg1MzYzfQ.1dwQQWsEqAs5kv2KjbThtvuGhi5P7e1IA2SYOg_BB9s; _ga_3WZHLSR928=GS1.1.1695883982.8.1.1695885363.0.0.0',
        }
filter = '//div[@class="im"]/a/@href'
filter1 = '/html/body/div[1]/main/div/div[2]/h1'
keywords = []
blacklist = ['google', 'apache', 'io', 'org', 'com']

common_rules = '''
A.B.C -> A.B:C
A.B.C -> A.B:B-C
A.B.C -> A.B.C:C
'''

keywords = open(key , 'r').readlines()
con = sqlite3.connect(db)
cur = con.cursor()
cur.execute('select prefix from TPL')
prefix = cur.fetchall()
prefix = [i[0] for i in prefix]

tmp = [0, 0, 0]

def check_in_base(st):
    for pre in prefix:
        if(st.startswith(pre)):
            return True
        

def subcheck(group, artifact):
    
    if artifact in blacklist:
        return False
    url = f'https://mvnrepository.com/artifact/{group}/{artifact}'
    res = requests.get(url, headers=headers)

    if(res.status_code==200):
        tmp[0] = group
        tmp[1] = artifact
        prefix.append(tmp[2])
        print(f'result: {tmp}')
        cur.execute("INSERT INTO TPL VALUES (?,?,?)", (tmp[0], tmp[1], tmp[2]))
        con.commit()
        return True
    else:
        return False
    
    return False

def check_if_tpl(w, l):
    tmp[2] = '.'.join(w)
    if(l==1):
        subcheck(w[0], w[0])
    else:
        if(subcheck('.'.join(w), w[-1])):
            return
        if(subcheck('.'.join(w[:l-1]), w[-1])):
            return
        if(subcheck('.'.join(w[:l-1]), w[-2]+'-'+w[-1])):
            return
        check_if_tpl(w[:-1], l-1)
    

for word in keywords:
    if(check_in_base(word)):
        continue
    pre = word.replace('\n','').split('.')
    check_if_tpl(pre, len(pre))

con.close()
