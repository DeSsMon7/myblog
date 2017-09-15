/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ash.myblog23.control;

import com.ash.myblog23.model.Post;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author Timmy
 */
@Stateless
public class PostFacade extends AbstractFacade<Post> {

    @PersistenceContext(unitName = "com.ash_myblog23_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PostFacade() {
        super(Post.class);
    }

    public void createPost(String title, String content, Date postdate, Integer users_id) {
        Query query = em.createNativeQuery("INSERT INTO public.posts(title, content, postdate, users_id) VALUES (?1, ?2, ?3, ?4)");
        int updateCount = query
                .setParameter(1, title)
                .setParameter(2, content)
                .setParameter(3, postdate, TemporalType.TIMESTAMP)
                .setParameter(4, users_id)
                .executeUpdate();
    }

    public List findAllOrdered() {
        return em.createQuery("SELECT p FROM Post p ORDER BY p.postDate DESC ")
                .getResultList();
    }
    
    public List findByPostId(Integer id) {
        List result = (List)  em.createQuery("SELECT p FROM Post p WHERE p.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList();
        return result;
    }
}
