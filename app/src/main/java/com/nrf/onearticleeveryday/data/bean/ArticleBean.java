package com.nrf.onearticleeveryday.data.bean;

/**
 * Project: OneArticleEveryday
 * Author:  NRF
 * Version:  1.0
 * Date:    2017/5/22
 * Modify:  //TODO
 * Description: //TODO
 * Copyright notice:
 */
public class ArticleBean {
    public DataInfo getData() {
        return data;
    }

    public void setData(DataInfo data) {
        this.data = data;
    }

    public DataInfo data;

    public static class DataInfo {
        public DateInfo date;
        public String author;

        public void setWc(int wc) {
            this.wc = wc;
        }

        public void setDate(DateInfo date) {
            this.date = date;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String title;
        public String digest;

        public int getWc() {
            return wc;
        }

        public DateInfo getDate() {
            return date;
        }

        public String getAuthor() {
            return author;
        }

        public String getTitle() {
            return title;
        }

        public String getDigest() {
            return digest;
        }

        public String getContent() {
            return content;
        }

        public String content;
        public int wc;

        public static class DateInfo {
            public String curr;
            public String prev;
            public String next;

            public void setCurr(String curr) {
                this.curr = curr;
            }

            public void setPrev(String prev) {
                this.prev = prev;
            }

            public void setNext(String next) {
                this.next = next;
            }

            public String getCurr() {
                return curr;
            }

            public String getPrev() {
                return prev;
            }

            public String getNext() {
                return next;
            }
        }
    }
}
