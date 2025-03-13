codeType = ["abcdefghijklmnopqrstuwxyz0123456789",
            "0123456789abcdefghijklmnopqrstuwxyz",
            "abcdefghijklmnopqrstuqwxyzABCDEFGHIJKLMNOPQRSTUWXYZ"]
to_code = "ASDASD"

 
move = 34

print(len(to_code))

def Cesar(alphabet_idx, to_code, move, isDecoding):
    alphabet = codeType[alphabet_idx]
    output = ""
    if(alphabet_idx!=2):
        to_code_lower = to_code.lower()
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
    print(output)
    return output


def Cesar2():
    print("WTF")

coded = Cesar(2, to_code, 3, True)
decoded = Cesar(2, coded, 3, False)


    