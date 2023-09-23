package Part_Of_Speech_Tagging;

import lingologs.Texture;

public class test {
    public static void main(String[] args) {
        Texture<String> S = new Texture<>();
        S = S.add("asdf");
        S = S.add("asdf");
        System.out.println(S.toList().size());
    }
}
