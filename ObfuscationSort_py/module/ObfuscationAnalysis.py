from config import keyword_path

class ObfAna(object):

    def __init__(self):
        self.whiteList = ["os", "io", "ui"]
        with open(keyword_path, 'r') as f:
            wl = f.readlines()
            self.wordList = [i.strip() for i in wl]
    
    def isObfuscate(self, st):
        word = st.lower()
        if('$' in word):
            return True
        if(len(word) >= 5):
            return False
        if((len(word)<=2) & (word not in self.whiteList)):
            return True
        return word not in self.wordList