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
        return switch (intValue) {
            case 0 -> Noun;
            case 1 -> Conjunction;
            case 2 -> Numeral;
            case 3 -> Determiner;
            case 4 -> Adjective;
            case 5 -> Adverb;
            case 6 -> to;
            case 7 -> Verb;
            case 8 -> Existential;
            case 9 -> Preposition;
            case 10 -> list;
            case 11 -> Auxilary;
            case 12 -> Pronoun;
            case 13 -> Interjection;
            default -> Default;
        };
    }
}
