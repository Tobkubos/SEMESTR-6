import collections
import matplotlib.pyplot as plt
import numpy as np

codeType = ["abcdefghijklmnopqrstuwxyz0123456789",
            "0123456789abcdefghijklmnopqrstuwxyz",
            "abcdefghijklmnopqrstuqwxyzABCDEFGHIJKLMNOPQRSTUWXYZ"]
to_code = "ASDASD"


f = open("Frankenstein.txt", "r", encoding="UTF-8")
#print(f.read())
 
to_code = f.read()

#print(len(to_code))

def Cesar(alphabet_idx, to_code, move, isDecoding):
    alphabet = codeType[alphabet_idx]
    output = ""
    if(alphabet_idx!=2):
        to_code_lower = to_code.lower()
    else:
        to_code_lower = to_code
    for i in to_code_lower:
        for j in alphabet:
            if(i == j and i in alphabet):
                if(isDecoding):
                    if(alphabet.find(i) + move >= len(alphabet)):
                        new_index = alphabet.find(i) + move - len(alphabet)
                        output += alphabet[new_index]
                    else:
                        output += alphabet[alphabet.find(i) + move]
                else:
                    if(alphabet.find(i) - move < 0):
                        new_index = len(alphabet) + alphabet.find(i) - move
                        output += alphabet[new_index]
                    else:
                        output += alphabet[alphabet.find(i) - move]
            elif(i not in alphabet):
                output+=i
                break
    #print(output)
    return output


coded = Cesar(0, to_code, 3, True)

def pokazPlot(licznik): 
    
    litery = list(licznik.keys())  # Unikalne litery
    wystapienia = list(licznik.values())  # Ich liczba wystąpień
           
    plt.figure(figsize=(10, 5))
    plt.bar(litery, wystapienia, color='skyblue', edgecolor='black')

    plt.xlabel("Litery")
    plt.ylabel("Liczba wystąpień")
    plt.title("Częstotliwość występowania liter w tekście")
    plt.grid(axis="y", linestyle="--", alpha=0.7)

    plt.show()    
#------------------------------------------------------

def sprawdzCzestotliwosc(tekst):    
    tekstDoLiczenia = tekst.lower()
    licznik = collections.Counter(char for char in tekstDoLiczenia if char.isalpha())


    #for litera, liczba in licznik.items(): 
        #print(f"'{litera}': {liczba}")

    suma_wszystkich_liter = sum(licznik.values())
    czestotliwosci = {litera: liczba / suma_wszystkich_liter for litera, liczba in licznik.items()}
    czestotliwosci_sum_kwadr = {litera: freq ** 2 for litera, freq in czestotliwosci.items()}
    return (sum(czestotliwosci_sum_kwadr.values()))
#------------------------------------------------------

#for i in range (len(codeType)):
for j in range(len(codeType[1])):
    decoded = Cesar(0, coded, j, False)
    r = sprawdzCzestotliwosc(decoded)
    print(r, " --> ", j)
#decoded = Cesar(1, coded, 3, False)
#print(decoded)


    