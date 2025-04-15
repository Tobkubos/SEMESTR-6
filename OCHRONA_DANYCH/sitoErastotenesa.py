import math
import time
vect = []

def sitoErastotenesa(n):
    for i in range (2,n+1):
        vect.append(i)

    lastElement = int(math.sqrt(n))
    for i in range(0, lastElement):
        for j in range(len(vect)-1, i, -1):
            #print(j,i)
            #print("porównuje ", vect[j], "", vect[i])
            if vect[j] % vect[i] == 0:
                #print("usun!", vect[j])
                vect.pop(j)

    print("liczby pierwsze", vect)
    print("długość", len(vect))
    print("suma liczb",sum(vect))
    # above70k = []
    # for i in vect:
    #     if i>70000:
    #         above70k.append(i)
    # print("liczby pierwsze", above70k)
    # print("długość", len(above70k))
    # print("suma liczb",sum(above70k))

def LucasLehmer(p):
    ops = 0
    if p == 2:
        return True, ops
    M = 2**p -1
    s = 4
    for _ in range(p - 2):
        s = (s * s - 2) % M
        ops += 3
    return s == 0, ops

sum_ops = 0
sum_primes = 0
start = time.perf_counter()
for p in range(2, 20):
    is_prime, ops = LucasLehmer(p)
    sum_ops += ops
    if is_prime:
        sum_primes +=1
        print(f"2^{p} - 1 = {2**p - 1} jest pierwsza (liczba Mersenne’a)")
end = time.perf_counter()
elapsed = end - start
print("ILOSC LICZB P", sum_primes, "\nSUMA OPERACJI",sum_ops, "\nCZAS DZIAŁANIA", elapsed)


start = time.perf_counter()
sitoErastotenesa(20)
end = time.perf_counter()
elapsed = end - start
print("CZAS DZIAŁANIA", elapsed)