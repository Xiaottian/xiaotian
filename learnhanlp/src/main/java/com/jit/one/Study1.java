/**
 * ProjectName:    MyTestProject
 * PackageName:    com.jit.one
 * FileName：      Study1.java
 * Copyright:      Copyright(C) 2018
 * Company:        北京神州泰岳软件股份有限公司
 * Author:         JIT
 * CreateDate:     2018/10/10 9:40
 */

package com.jit.one;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.TraditionalChineseTokenizer;

import java.util.List;

public class Study1 {
    public static void main(String[] args) {
        //1.  demo1
        System.out.println(HanLP.segment("你好，欢迎使用HanLP！"));

        //2.  标准分词
        List<Term> termList = HanLP.segment("商品和服务");
        List<Term> termList00 = HanLP.segment("我和你");
        System.out.println(termList00);

        //3.  NLP分词
//        执行全部命名实体识别和词性标注。
//        所以速度比标准分词慢，并且有误识别的情况
        List<Term> termList1 = NLPTokenizer.segment("中国科学院计算技术研究所的宗成庆教授正在教授自然语言处理课程");
        System.out.println(termList1);

//       4. 索引分词 IndexTokenizer 是面向搜索引擎的分词器，能够对长词全切分，另外通过 term.offset 可以获取单词在文本中的偏移量
        List<Term> termList4 = IndexTokenizer.segment("主副食品");
        for (Term term : termList4)
        {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }

        // 5.繁体分词 TraditionalChineseTokenizer 可以直接对繁体进行分词，输出切分后的繁体词语。
        List<Term> termList5 = TraditionalChineseTokenizer.segment("大衛貝克漢不僅僅是名著名球員，球場以外，其妻為前辣妹合唱團成員維多利亞·碧咸，亦由於他擁有突出外表、百變髮型及正面的形象，以至自己品牌的男士香水等商品，及長期擔任運動品牌Adidas的代言人，因此對大眾傳播媒介和時尚界等方面都具很大的影響力，在足球圈外所獲得的認受程度可謂前所未見。");
        System.out.println(termList5);

        System.out.println("---------------下边是极速分词-------------------");
        // 6.极速词典分词  词典最长分词，速度极其快，精度一般。
        String text = "江西鄱阳湖干枯，中国最大淡水湖变成大草原";
        System.out.println(SpeedTokenizer.segment(text));
        long start = System.currentTimeMillis();
        int pressure = 1000000;
        for (int i = 0; i < pressure; ++i)
        {
            SpeedTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double)1000;
        System.out.printf("分词速度：%.2f字每秒", text.length() * pressure / costTime);

        System.out.println("---------------7.下边是N最短路径分词器-------------------");
        // 7.N最短路径分词器 NShortSegment 比最短路分词器( DijkstraSegment )慢，但是效果稍微好一些，对命名实体识别能力更强
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new ViterbiSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        String[] testCase = new String[]{
                "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
        };
        for (String sentence : testCase)
        {
            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路径分词：" + shortestSegment.seg(sentence));
        }

        System.out.println("---------------8.下边是CRF分词-------------------");

        // 8.CRF分词  基于CRF模型和BEMS标注训练得到的分词器
        Segment segment = new CRFSegment();
        segment.enablePartOfSpeechTagging(true);
        List<Term> termList8 = segment.seg("你看过穆赫兰道吗");
        System.out.println(termList8);
        for (Term term : termList8)
        {
            if (term.nature == null)
            {
                System.out.println("识别到新词：" + term.word);
            }
        }

    }
}
