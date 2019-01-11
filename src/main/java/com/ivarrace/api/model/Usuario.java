package com.ivarrace.api.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "users")
public class Usuario {

    @Id
    private ObjectId _id;
    @Indexed
    private String alias;
    private String name;

    public String get_id() { return _id!=null ?_id.toHexString() : ""; }

    public void set_id(ObjectId id) { this._id = id; }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return get_id().equals(usuario.get_id()) &&
                Objects.equals(getAlias(), usuario.getAlias()) &&
                Objects.equals(getName(), usuario.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id(), getAlias(), getName());
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "_id='" + get_id() +'\'' +
                " alias='" + alias + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
