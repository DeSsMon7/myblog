package com.ash.myblog23.model;

import com.ash.myblog23.model.User;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-08-05T17:00:28")
@StaticMetamodel(Post.class)
public class Post_ { 

    public static volatile SingularAttribute<Post, User> usersId;
    public static volatile SingularAttribute<Post, Date> postDate;
    public static volatile SingularAttribute<Post, Integer> id;
    public static volatile SingularAttribute<Post, String> title;
    public static volatile SingularAttribute<Post, String> content;

}