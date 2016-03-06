package com.bolezhai.tutorial.solr;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

/**
 * Created by colin on 16/3/3.
 */
public class IndexWriterDemo {

    public static void main(String[] args) throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_34, new StandardAnalyzer(Version.LUCENE_34));
        IndexWriter writer = new IndexWriter(FSDirectory.open(new File("/Users/colin/tmp")),config);

        Document doc = new Document();
        doc.add(new Field("text", "hello workd", Field.Store.NO, Field.Index.ANALYZED));

        writer.addDocument(doc);

        writer.commit();
        writer.close();

    }
}
