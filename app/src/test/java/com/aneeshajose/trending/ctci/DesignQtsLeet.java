package com.aneeshajose.trending.ctci;

import com.google.common.truth.Truth;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Aneesha Jose on 2020-05-26.
 */
public class DesignQtsLeet {
    @Test
    public void test_AutocompleteSystem() {
        AutocompleteSystem autocompleteSystem = new AutocompleteSystem(new String[]{"i love you","island","iroman","i love leetcode"},
                new int[]{5,3,2,2});
        System.out.println(autocompleteSystem.input('i'));
        System.out.println(autocompleteSystem.input(' '));
        System.out.println(autocompleteSystem.input('a'));
        System.out.println(autocompleteSystem.input('#'));
        System.out.println(autocompleteSystem.input('i'));
        System.out.println(autocompleteSystem.input(' '));
        System.out.println(autocompleteSystem.input('a'));
        System.out.println(autocompleteSystem.input('#'));
        System.out.println(autocompleteSystem.input('i'));
        System.out.println(autocompleteSystem.input(' '));
        System.out.println(autocompleteSystem.input('a'));
        System.out.println(autocompleteSystem.input('#'));
    }

    class AutocompleteSystem {

        List<Sentence> sentences;
        StringBuilder lastSent = new StringBuilder();

        public AutocompleteSystem(String[] sentences, int[] times) {
            this.sentences = new ArrayList<>(sentences.length);
            for(int i=0;i<sentences.length;i++){
                this.sentences.add(new Sentence(sentences[i],i < times.length ? times[i]:0));
            }
            Collections.sort(this.sentences, (a, b) -> b.compare(a));
        }

        public List<String> input(char c) {
            List<String> autoComp = new ArrayList<>(3);
            if(c == '#'){
                lastSent.delete(0, lastSent.length());
            } else{
                lastSent.append(c);
                int i = 0;
                String prefix = lastSent.toString();
                while(i < sentences.size() && autoComp.size() < 3){
                    if(sentences.get(i).startsWith(prefix)) autoComp.add(sentences.get(i).sentence);
                    i++;
                }
            }
            return autoComp;
        }
    }

    class Sentence{
        String sentence;
        int hotness;

        public Sentence(String sent, int hotness){
            this.sentence = sent;
            this.hotness = hotness;
        }

        public int compare(Sentence s){
            if(hotness == s.hotness) return s.sentence.compareTo(sentence);
            else return hotness - s.hotness;
        }

        public boolean startsWith(String prefix){
            return sentence != null && sentence.startsWith(prefix);
        }
    }
}
