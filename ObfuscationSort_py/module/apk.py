import datetime
import os

from ObfuscationAnalysis import ObfAna
from androguard.core.apk import APK
from androguard.core.dex import DEX
from Utils import *

class Apk(object):

    def __init__(self, apk_path):
        self.analyzer = ObfAna()
        self.nameSet = set()
        self.name = os.path.basename(apk_path)
        self.parse_apk(apk_path)

    def parse_apk(self, apk_path):
        start = datetime.datetime.now()
        
        apk_obj = APK(apk_path)
        
        end = datetime.datetime.now()
        print(f'{self.name} decompile finished, cost {end-start}')

        start = datetime.datetime.now()
        self.parse_class_name(apk_obj)
        end = datetime.datetime.now()
        print(f'{self.name} parsing ended, cost {end-start}')

    def parse_class_name(self, obj):
        for dex in obj.get_all_dex():
            try:
                dex_obj = DEX(dex)
            except Exception:
                return
            
            for cls in dex_obj.get_classes():
                class_name = cls.get_name().replace("/", ".")[1:-1]
                if(isAndroidClass(class_name)):
                    continue
                if(isResourceClass(class_name)):
                    continue
                self.parse_word(class_name)

    def parse_word(self, class_name):
        group = class_name.split('.')
        tmp = []
        for i in range(len(group)):
            if(i==4):
                break
            if(self.analyzer.isObfuscate(group[i])):
                break
            tmp.append(group[i])
            if(i==0):
                continue
            
            self.nameSet.add('.'.join(tmp))

    def printRes(self):
        print(self.nameSet)

    def getClass(self):
        return self.nameSet