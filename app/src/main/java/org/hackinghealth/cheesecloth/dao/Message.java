package org.hackinghealth.cheesecloth.dao;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by miantorno on 6/5/17.
 */

public class Message extends RealmObject {

    private Sender sender;
    private String text;
    private Date date;

    public Message() {

    }

    private Message(Builder builder) {
        setSender(builder.sender);
        setText(builder.text);
        setDate(builder.date);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Message copy) {
        Builder builder = new Builder();
        builder.sender = copy.sender;
        builder.text = copy.text;
        builder.date = copy.date;
        return builder;
    }

    public Sender getSender() {
        return sender;
    }

    public Message setSender(Sender sender) {
        this.sender = sender;
        return this;
    }

    public String getText() {
        return text;
    }

    public Message setText(String text) {
        this.text = text;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Message setDate(Date date) {
        this.date = date;
        return this;
    }

    /**
     * {@code Message} builder static inner class.
     */
    public static final class Builder {
        private Sender sender;
        private String text;
        private Date date;

        private Builder() {
        }

        /**
         * Sets the {@code sender} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code sender} to set
         * @return a reference to this Builder
         */
        public Builder sender(Sender val) {
            sender = val;
            return this;
        }

        /**
         * Sets the {@code text} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code text} to set
         * @return a reference to this Builder
         */
        public Builder text(String val) {
            text = val;
            return this;
        }

        /**
         * Sets the {@code date} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param val the {@code date} to set
         * @return a reference to this Builder
         */
        public Builder date(Date val) {
            date = val;
            return this;
        }

        /**
         * Returns a {@code Message} built from the parameters previously set.
         *
         * @return a {@code Message} built with parameters of this {@code Message.Builder}
         */
        public Message build() {
            return new Message(this);
        }
    }
}