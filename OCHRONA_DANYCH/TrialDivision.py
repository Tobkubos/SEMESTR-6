import math

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

print(SprawdzLiczbyPierwszeWPrzedziale(2, 100))

def ZnajdzCzynnikiPierwsze(liczba):
    liczbyPierwsze = SprawdzLiczbyPierwszeWPrzedziale(1, liczba)
    czynnikiPierwsze = []
    for i in liczbyPierwsze:
        if liczba % i == 0:
            czynnikiPierwsze.append(i)
    print(SprawdzProduktCzynnikowPierwszych(czynnikiPierwsze, liczba))
    return czynnikiPierwsze

def SprawdzProduktCzynnikowPierwszych(czynnikiPierwsze, liczba):
    p = math.prod(czynnikiPierwsze)
    if p == liczba:
        return True
    return False


#print(ZnajdzCzynnikiPierwsze(50))