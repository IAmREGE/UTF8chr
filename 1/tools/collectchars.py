from typing import Final, List

SPINS: Final[str] = r"\|/-"
BAR: Final[str] = "="

DECIMALS: Final[List[int]] = []
DIGITS: Final[List[int]] = []
NUMERICS: Final[List[int]] = []
WHITESPACES: Final[List[int]] = []

o: int
c: str
for i in range(68) :
    for j in range(4) :
        for k in range(4096) :
            o = (i << 14) | (j << 12) | k
            c = chr(o)
            if c.isdecimal() :
                DECIMALS.append(o)
            if c.isdigit() :
                DIGITS.append(o)
            if c.isnumeric() :
                NUMERICS.append(o)
            if c.isspace() :
                WHITESPACES.append(o)
        print(SPINS[j], end="\b", flush=True)
    print(end=BAR, flush=True)
print()
print("Decimals:", DECIMALS)
print("Digits:", DIGITS)
print("Numerics:", NUMERICS)
print("Whitespaces:", WHITESPACES)