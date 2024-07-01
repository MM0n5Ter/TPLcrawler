def isResourceClass(st):
    return st.endswith(".R")

def isAndroidClass(st):
    androidPrefixPkgNames = ["android.", "com.google.android.", "com.android.", "androidx.",
                "kotlin.", "kotlinx.", "java.", "javax.", "sun.", "com.sun.", "jdk.", "j$.",
                "org.omg.", "org.xml.", "org.w3c.dom"]
    for pre in androidPrefixPkgNames:
        if(st.startswith(pre)):
            return True
    return False