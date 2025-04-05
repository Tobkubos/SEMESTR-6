import math

"""
Funkcje w tym skrypcie:

1. JestLiczbaPierwsza(liczba) -> bool
   Sprawdza, czy podana liczba jest pierwsza.

2. SprawdzLiczbyPierwszeWPrzedziale(start, koniec) -> list[int]
   Zwraca listę liczb pierwszych w zadanym przedziale.

3. ZnajdzCzynnikiPierwsze(liczba) -> list[int]
   Zwraca listę czynników pierwszych danej liczby.

4. SprawdzProduktCzynnikowPierwszych(czynnikiPierwsze, liczba) -> bool
   Sprawdza, czy iloczyn podanych czynników pierwszych daje oryginalną liczbę.
"""


def JestLiczbaPierwsza(liczba):
    end = math.floor(math.sqrt(liczba))
    for i in range(2, end+1):
        if liczba % i == 0:
            return False
    return True

print(JestLiczbaPierwsza(130))

def SprawdzLiczbyPierwszeWPrzedziale(start, koniec):
    liczbyPierwsze = []
    for i in range(start, koniec):
        check = JestLiczbaPierwsza(i)
        if check == True:
            liczbyPierwsze.append(i)
    return liczbyPierwsze

def ZnajdzCzynnikiPierwsze(liczba):
    f_liczba = liczba
    liczbyPierwsze = SprawdzLiczbyPierwszeWPrzedziale(2, f_liczba)
    print(liczbyPierwsze)
    czynnikiPierwsze = []

    kroki = 0
    i = 0
    t = liczbyPierwsze[i]

    while f_liczba > 1 and i < len(liczbyPierwsze):
            t = liczbyPierwsze[i]
            kroki += 1
            while f_liczba % t == 0:
                czynnikiPierwsze.append(t)
                f_liczba = f_liczba // t
                kroki += 1
            i += 1

    print(SprawdzProduktCzynnikowPierwszych(czynnikiPierwsze, liczba))
    return czynnikiPierwsze, kroki

def SprawdzProduktCzynnikowPierwszych(czynnikiPierwsze, liczba):
    p = math.prod(czynnikiPierwsze)
    if p == liczba:
        return True
    return False

def ZnajdzNWD(liczba1, liczba2):
    nwd = 1
    min_num = min(liczba1, liczba2)

    for i in range(1, min_num+1):
        if liczba1 % i == 0 and liczba2 % i == 0:
            nwd = i
    return nwd


czynniki, liczba_kroków = ZnajdzCzynnikiPierwsze(90)
print("Czynniki:", czynniki)
print("Liczba wykonanych kroków:", liczba_kroków)

print(ZnajdzNWD(56,100))