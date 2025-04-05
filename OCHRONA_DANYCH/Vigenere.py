import collections
import matplotlib.pyplot as plt
import numpy as np
import math
import operator

codeType = "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ"

to_code = "abcdefghijklmnopqrstuwxyzABCDEFGHIJKLMNOPQRSTUWXYZ"
key = "Kupka"
coded = ""

f = open("Frankenstein.txt", "r", encoding="UTF-8")
to_code = f.read()

def VigenereCode(to_code, key, isCoding):
    alphabet = codeType
    output = ""
    key_index = 0
    key_length = len(key)
    
    for char in to_code:
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

coded = VigenereCode(to_code, key, True)
decoded = VigenereCode(coded, key, False)

print(coded)
print(codeType)
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




#########################################################
#
#               ATAK DESZYFRUJĄCY!
#
#########################################################

def find_repeated_sequences(text, seq_len):
    """Znajduje powtarzające się sekwencje o danej długości i ich pozycje."""
    sequences = collections.defaultdict(list)
    for i in range(len(text) - seq_len + 1):
        seq = text[i:i+seq_len]
        sequences[seq].append(i)
    return {seq: pos for seq, pos in sequences.items() if len(pos) > 1}

def get_sequence_spacings(sequences):
    """Oblicza odległości między wystąpieniami tych samych sekwencji."""
    spacings = []
    for seq, positions in sequences.items():
        for i in range(len(positions) - 1):
            spacings.append(positions[i+1] - positions[i])
    return spacings

def get_factors(number):
    """Znajduje wszystkie dzielniki liczby większe od 1."""
    factors = set()
    for i in range(2, int(math.sqrt(number)) + 1):
        if number % i == 0:
            factors.add(i)
            factors.add(number // i)
    # Sama liczba też jest swoim dzielnikiem (jeśli > 1)
    if number > 1:
         factors.add(number)
    return list(factors)

def estimate_key_length(ciphertext, min_len=3, max_len=20, min_seq_len=3, max_seq_len=5):
    """Estymuje długość klucza metodą Kasiskiego."""
    filtered_ciphertext = "".join(c for c in ciphertext if c in codeType)
    if not filtered_ciphertext:
        print("Brak znaków z alfabetu w szyfrogramie do analizy.")
        return None

    all_spacings = []
    for seq_len in range(min_seq_len, max_seq_len + 1):
        sequences = find_repeated_sequences(filtered_ciphertext, seq_len)
        spacings = get_sequence_spacings(sequences)
        all_spacings.extend(spacings)

    if not all_spacings:
        print("Nie znaleziono powtarzających się sekwencji. Metoda Kasiskiego może zawieść.")
        return None # Lub zwrócić domyślną/próbować IC

    possible_key_lengths = collections.Counter()
    for space in all_spacings:
        factors = get_factors(space)
        for factor in factors:
            if min_len <= factor <= max_len:
                possible_key_lengths[factor] += 1

    if not possible_key_lengths:
        print(f"Nie znaleziono potencjalnych długości klucza w zakresie [{min_len}, {max_len}].")
        return None

    # Zwracamy najbardziej prawdopodobną długość klucza
    # Sortujemy wg częstości malejąco, potem wg długości rosnąco (w razie remisu)
    sorted_lengths = sorted(possible_key_lengths.items(), key=lambda item: (-item[1], item[0]))
    # print("Prawdopodobne długości klucza (długość: liczba wskazań):", sorted_lengths) # Debug
    most_likely_length = sorted_lengths[0][0]
    return most_likely_length


def analyze_frequency(text, alphabet):
    """Oblicza częstość występowania liter z danego alfabetu w tekście."""
    counts = collections.Counter(c for c in text if c in alphabet)
    total = sum(counts.values())
    if total == 0:
        return {}
    # Zwracamy posortowane od najczęstszej
    frequencies = {char: count / total for char, count in counts.items()}
    sorted_freq = dict(sorted(frequencies.items(), key=operator.itemgetter(1), reverse=True))
    return sorted_freq

def find_key(ciphertext, key_length, alphabet):
    """Odnajduje klucz na podstawie analizy częstości pod-szyfrów."""
    if key_length is None or key_length <= 0:
         return None
    recovered_key = ""
    # Zakładamy, że najczęstsza litera w języku angielskim to 'e'.
    # Musimy znaleźć jej odpowiednik w naszym 'codeType'
    # Jeśli 'e' jest w alfabecie, użyj 'e'. Jeśli nie, wybierz inną (np. 'a').
    assumed_most_freq_plain_char = 'e' if 'e' in alphabet else alphabet[0]
    assumed_most_freq_plain_idx = alphabet.find(assumed_most_freq_plain_char)
    if assumed_most_freq_plain_idx == -1:
        print(f"Błąd: Założony najczęstszy znak '{assumed_most_freq_plain_char}' nie występuje w alfabecie.")
        # W tym przypadku można by wziąć po prostu indeks 0 jako odniesienie.
        # assumed_most_freq_plain_idx = 0 # Alternatywa
        return None # Lub zwrócić błąd

    # Filtrujemy szyfrogram, zostawiając tylko znaki z alfabetu
    filtered_ciphertext = "".join(c for c in ciphertext if c in alphabet)
    if not filtered_ciphertext:
        return None # Nie ma czego analizować

    for i in range(key_length):
        # Wyodrębniamy pod-szyfr (co L-tą literę, zaczynając od i)
        sub_cipher = filtered_ciphertext[i::key_length]
        if not sub_cipher:
            # Może się zdarzyć dla krótkich tekstów lub długich kluczy
             print(f"Ostrzeżenie: Pod-szyfr {i+1} jest pusty.")
             # Można dodać znak-wypełniacz lub zgłosić błąd
             # Na razie pomijamy lub dodajemy np. pierwszą literę alfabetu
             recovered_key += alphabet[0] # Proste obejście, może być niedokładne
             continue

        # Analizujemy częstość w pod-szyfrze
        freq = analyze_frequency(sub_cipher, alphabet)
        if not freq:
             print(f"Ostrzeżenie: Nie udało się przeanalizować częstości dla pod-szyfru {i+1}.")
             recovered_key += alphabet[0] # Proste obejście
             continue

        # Najczęstsza litera w tym pod-szyfrze
        most_freq_cipher_char = list(freq.keys())[0]
        most_freq_cipher_idx = alphabet.find(most_freq_cipher_char)

        # Obliczamy przesunięcie (indeks litery klucza)
        # shift = (indeks_zaszyfrowanej - indeks_oryginalnej) mod długość_alfabetu
        key_char_index = (most_freq_cipher_idx - assumed_most_freq_plain_idx) % len(alphabet)
        recovered_key += alphabet[key_char_index]

    return recovered_key


MIN_KEY_LEN = 3
MAX_KEY_LEN = 15

estimated_len = estimate_key_length(coded, min_len=MIN_KEY_LEN, max_len=MAX_KEY_LEN)

if estimated_len:
    print(f"Przypuszczalna długość klucza: ", estimated_len)

    # Krok 2: Odnalezienie klucza
    recovered_key = find_key(coded, estimated_len, codeType)

    if recovered_key:
        print(f"Odzyskany klucz: ",recovered_key)

        # Sprawdzenie, czy odzyskany klucz pasuje (opcjonalne)
        if recovered_key.lower() == key.lower(): # Porównujemy bez względu na wielkość liter
            print("Klucz jest zgodny z oryginalnym.")
        else:
            print("Klucz się różni od oryginału")

        # Dekodowanie
        try:
            decoded_text = VigenereCode(coded, recovered_key, False)
            print("ODsZYFROWANY FRAGMENT: ", decoded_text[:200])

            # Proste sprawdzenie poprawności deszyfrowania
            if decoded_text[:100] == to_code[:100]:
                print("OKEJ")
            else:
                print("NIE JEST OKEJ")

        except ValueError as e:
                print("BŁĄD: ", e)

    else:
        print("Nie udało się odzyskać klucza na podstawie oszacowanej długości.")
else:
    print("Nie udało się oszacować długości klucza. Atak nie powiódł się.")

