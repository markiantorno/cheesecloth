package org.hackinghealth.cheesecloth.dao;

import io.realm.RealmObject;

/**
 * Created by miantorno on 6/5/17.
 */

public class Sender extends RealmObject {

    public String name;
    public String address;

    public Sender() {

    }

    public Sender(String name, String address) {
        this.name = name;
        this.address = address;
    }

    private Sender(Builder builder) {
        setName(builder.name);
        setAddress(builder.address);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Sender copy) {
        Builder builder = new Builder();
        builder.name = copy.name;
        builder.address = copy.address;
        return builder;
    }

    public String getName() {
        return name;
    }

    public Sender setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Sender setAddress(String address) {
        this.address = address;
        return this;
    }

    /**
     * {@code Sender} builder static inner class.
     */
    public static final class Builder {
        private String name;
        private String address;

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder address(String val) {
            address = val;
            return this;
        }

        public Sender build() {
            return new Sender(this);
        }
    }

    @Override
    public String toString() {
        return "Sender{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
