package jpabook.start;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        //Entity Manager Factory 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(("jpabook"));

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            completeSaveAll(em);
//            testSaveAll(em);
//            testSave(em);
//            queryLogicJoin(em);
//            updateRelation(em);
//            queryLogicJoin(em);
//            deleteRelation(em);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

    public static void testSave(EntityManager em) {
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);


        Member member2 = new Member("member2", "회원2");
        member2.setTeam(team1);
        em.persist(member2);

    }

    //양방향 연관관계 설정
    public static void testSaveAll(EntityManager em) {
        Team team1 = new Team("team1", "팀1");

        Member member1 = new Member("member1", "회원1");
        Member member2 = new Member("member2", "회원2");

        member1.setTeam(team1);
        team1.getMembers().add(member1);

        member2.setTeam(team1);
        team1.getMembers().add(member2);

        List<Member> members = team1.getMembers();
        System.out.println("member.size = " + members.size());
    }

    public static void completeSaveAll(EntityManager em) {
        Team team1 = new Team("team1", "팀1");
        em.persist(team1);

        Member member1 = new Member("member1", "회원1");
        member1.setTeam(team1);
        em.persist(member1);

        Member member2 = new Member("member2", "회원2 ");
        member2.setTeam(team1);
        em.persist(member2);
    }

    private static void queryLogicJoin(EntityManager em) {
        String jpql = "select m from Member m join m.team t where t.name=:teamName";

        List<Member> resultList = em.createQuery(jpql, Member.class)
                .setParameter("teamName", "팀1").getResultList();

        for (Member member : resultList) {
            System.out.println("[query] member.username=" + member.getUsername());
        }
    }

    private static void updateRelation(EntityManager em) {
        Team team2 = new Team("team2", "팀2");
        Team team3 = new Team("team3", "팀3");
        em.persist(team2);
        em.persist(team3);

        Member member = em.find(Member.class, "member1");
        member.setTeam(team2);
    }

    private static void deleteRelation(EntityManager em) {
        Member member1 = em.find(Member.class, "member1");
        member1.setTeam(null);
    }
}
