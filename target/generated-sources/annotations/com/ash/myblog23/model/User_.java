package com.ash.myblog23.model;

import com.ash.myblog23.model.Post;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2018-05-18T20:18:08")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, String> password;
    public static volatile ListAttribute<User, Post> postList;
    public static volatile SingularAttribute<User, String> userAvatar;
    public static volatile SingularAttribute<User, String> userEmail;
    public static volatile SingularAttribute<User, Integer> id;
    public static volatile SingularAttribute<User, String> username;

}