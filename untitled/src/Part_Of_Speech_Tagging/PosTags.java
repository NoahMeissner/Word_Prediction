package Part_Of_Speech_Tagging;

public enum PosTags {

    Noun,
    Conjunction,
    Numeral,
    Determiner,
    Adjective,
    Adverb,
    to,
    Verb,
    Existential,
    Preposition,
    list,
    Auxilary,
    Pronoun,
    Interjection,
    Default;

    public static PosTags fromInt(int intValue) {
        switch (intValue) {
            case 0:
                return Noun;
            case 1:
                return Conjunction;
            case 2:
                return Numeral;
            case 3:
                return Determiner;
            case 4:
                return Adjective;
            case 5:
                return Adverb;
            case 6:
                return to;
            case 7:
                return Verb;
            case 8:
                return Existential;
            case 9:
                return Preposition;
            case 10:
                return list;
            case 11:
                return Auxilary;
            case 12:
                return Pronoun;
            case 13:
                return Interjection;
            default:
                return Default;
        }
    }
}
