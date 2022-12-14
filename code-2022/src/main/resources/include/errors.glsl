int out_ErrorPointer = 0;
int temp_NumToString = 0;
int[] temp_NumString = int[](0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

#define print(array) for (int i = 0; i < array.length(); i++) { out_Error[out_ErrorPointer++] = array[i]; }out_ErrorPointer--
#define printChar(character) out_Error[out_ErrorPointer++] = character;
#define printNumber(number) { \
temp_NumToString = number;                                                            \
 for (int i = 0; temp_NumToString > 0; temp_NumToString /= 10) { \
    temp_NumString[temp_NumString.length() - i++ - 1] = 48 + (temp_NumToString % 10); \
 }                                                                                     \
 for (int i = 0; i < temp_NumString.length(); i++) { \
    if(temp_NumString[i] != 0) { \
        printChar(temp_NumString[i]);                                            \
 }                                                                                 \
 }                                                                                     \
 }
