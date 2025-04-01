import collections
import matplotlib.pyplot as plt
import numpy as np

codeType = ["abcdefghijklmnopqrstuwxyz0123456789",
            "0123456789abcdefghijklmnopqrstuwxyz",
    "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ"]

to_code = "abcdefghijklmnopqrstuqwxyzABCDEFGHIJKLMNOPQRSTUWXYZ"
key = "toilet"
coded = ""

#f = open("Frankenstein.txt", "r", encoding="UTF-8")
#to_code = f.read()

def VigenereCode(alphabet_idx, to_code, key, isCoding):
    alphabet = codeType[alphabet_idx]
    output = ""
    key_index = 0
    key_length = len(key)
    
    if(alphabet_idx!=2): to_code_lower = to_code.lower() 
    else: to_code_lower = to_code
          
    for char in to_code_lower:
        if char in alphabet:
            char_index = alphabet.find(char)
            key_char_index = alphabet.find(key[key_index % key_length])
            if(isCoding):  
                new_index = (char_index + key_char_index) % len(alphabet)
            else:
                new_index = (char_index - key_char_index) % len(alphabet)  # Reverse shift
            output += alphabet[new_index]
            key_index += 1
        else:
            output += char
    
    return output

# Tworzenie dwóch wykresów na jednym obrazie

#fig, axes = plt.subplots(1, 2, figsize=(14, 5))

#axes[0].set_title("Częstotliwość liter w tekście zakodowanym")
#sprawdzCzestotliwosc_kwadr(coded, axes[0])

#axes[1].set_title("Częstotliwość liter w tekście odszyfrowanym")
#sprawdzCzestotliwosc_kwadr(decoded, axes[1])

#plt.tight_layout()
#plt.show()
coded = VigenereCode(2, to_code, key, True)
decoded = VigenereCode(2, coded, key, False)

print(coded)
print(codeType[2])
print(decoded)

def sprawdzCzestotliwosc_kwadr(tekst, ax=None):    
    tekstDoLiczenia = tekst.lower()
    licznik = collections.Counter(char for char in tekstDoLiczenia if char.isalpha())

    suma_wszystkich_liter = sum(licznik.values())
    czestotliwosci = {litera: liczba / suma_wszystkich_liter for litera, liczba in licznik.items()}
    czestotliwosci_sum_kwadr = {litera: freq ** 2 for litera, freq in czestotliwosci.items()}
    wynik = sum(czestotliwosci_sum_kwadr.values())

    if ax:
        pokazPlot(licznik, ax, wynik)  # Przekazujemy wynik do funkcji rysującej
    
    return wynik

def pokazPlot(licznik, ax, wynik): 
    litery = list(licznik.keys())  
    wystapienia = list(licznik.values())  

    ax.bar(litery, wystapienia, color='skyblue', edgecolor='black')
    ax.set_xlabel("Litery")
    ax.set_ylabel("Liczba wystąpień")
    ax.grid(axis="y", linestyle="--", alpha=0.7)

    # Dodanie wartości pod wykresem
    ax.text(0.5, -0.2, f'Częstotliwość sum kwadratów: {wynik:.6f}', 
            size=12, ha="center", transform=ax.transAxes, fontweight="bold", color="red")







