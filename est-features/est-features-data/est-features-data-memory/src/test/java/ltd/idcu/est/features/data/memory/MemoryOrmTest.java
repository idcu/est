package ltd.idcu.est.features.data.memory;

import ltd.idcu.est.features.data.api.Orm;
import ltd.idcu.est.features.data.api.Page;
import ltd.idcu.est.features.data.jdbc.TestUser;
import ltd.idcu.est.test.Assertions;
import ltd.idcu.est.test.annotation.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryOrmTest {
    
    @Test
    public void testCreation() {
        Orm orm = new MemoryOrm();
        Assertions.assertNotNull(orm);
    }
    
    @Test
    public void testSave() {
        Orm orm = new MemoryOrm();
        
        TestUser user = new TestUser("Alice", "alice@example.com", 25);
        TestUser saved = orm.save(user);
        
        Assertions.assertNotNull(saved);
        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Alice", saved.getName());
        Assertions.assertEquals("alice@example.com", saved.getEmail());
        Assertions.assertEquals(25, saved.getAge());
    }
    
    @Test
    public void testFindById() {
        Orm orm = new MemoryOrm();
        
        TestUser user = new TestUser("Bob", "bob@example.com", 30);
        TestUser saved = orm.save(user);
        
        Optional<TestUser> found = orm.find(TestUser.class, saved.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(saved.getId(), found.get().getId());
        Assertions.assertEquals("Bob", found.get().getName());
    }
    
    @Test
    public void testFindAll() {
        Orm orm = new MemoryOrm();
        
        orm.save(new TestUser("Alice", "alice@example.com", 25));
        orm.save(new TestUser("Bob", "bob@example.com", 30));
        orm.save(new TestUser("Charlie", "charlie@example.com", 35));
        
        List<TestUser> all = orm.findAll(TestUser.class);
        Assertions.assertEquals(3, all.size());
    }
    
    @Test
    public void testUpdate() {
        Orm orm = new MemoryOrm();
        
        TestUser user = new TestUser("Alice", "alice@example.com", 25);
        TestUser saved = orm.save(user);
        
        saved.setName("Alice Updated");
        saved.setAge(26);
        TestUser updated = orm.update(saved);
        
        Assertions.assertEquals("Alice Updated", updated.getName());
        Assertions.assertEquals(26, updated.getAge());
        
        Optional<TestUser> found = orm.find(TestUser.class, saved.getId());
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Alice Updated", found.get().getName());
    }
    
    @Test
    public void testDelete() {
        Orm orm = new MemoryOrm();
        
        TestUser user = new TestUser("Alice", "alice@example.com", 25);
        TestUser saved = orm.save(user);
        
        Assertions.assertTrue(orm.exists(TestUser.class, saved.getId()));
        
        orm.delete(saved);
        Assertions.assertFalse(orm.exists(TestUser.class, saved.getId()));
    }
    
    @Test
    public void testDeleteById() {
        Orm orm = new MemoryOrm();
        
        TestUser user = new TestUser("Alice", "alice@example.com", 25);
        TestUser saved = orm.save(user);
        
        Assertions.assertTrue(orm.exists(TestUser.class, saved.getId()));
        
        int deleted = orm.deleteById(TestUser.class, saved.getId());
        Assertions.assertEquals(1, deleted);
        Assertions.assertFalse(orm.exists(TestUser.class, saved.getId()));
    }
    
    @Test
    public void testCount() {
        Orm orm = new MemoryOrm();
        
        Assertions.assertEquals(0, orm.count(TestUser.class));
        
        orm.save(new TestUser("Alice", "alice@example.com", 25));
        orm.save(new TestUser("Bob", "bob@example.com", 30));
        
        Assertions.assertEquals(2, orm.count(TestUser.class));
    }
    
    @Test
    public void testExists() {
        Orm orm = new MemoryOrm();
        
        TestUser user = new TestUser("Alice", "alice@example.com", 25);
        TestUser saved = orm.save(user);
        
        Assertions.assertTrue(orm.exists(TestUser.class, saved.getId()));
        Assertions.assertFalse(orm.exists(TestUser.class, 999L));
    }
    
    @Test
    public void testFindByField() {
        Orm orm = new MemoryOrm();
        
        orm.save(new TestUser("Alice", "alice@example.com", 25));
        orm.save(new TestUser("Bob", "bob@example.com", 30));
        orm.save(new TestUser("Alice", "alice2@example.com", 35));
        
        List<TestUser> found = orm.findByField(TestUser.class, "name", "Alice");
        Assertions.assertEquals(2, found.size());
    }
    
    @Test
    public void testFindOneByField() {
        Orm orm = new MemoryOrm();
        
        orm.save(new TestUser("Alice", "alice@example.com", 25));
        orm.save(new TestUser("Bob", "bob@example.com", 30));
        
        Optional<TestUser> found = orm.findOneByField(TestUser.class, "email", "bob@example.com");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("Bob", found.get().getName());
    }
    
    @Test
    public void testFindByFields() {
        Orm orm = new MemoryOrm();
        
        orm.save(new TestUser("Alice", "alice@example.com", 25));
        orm.save(new TestUser("Bob", "bob@example.com", 30));
        orm.save(new TestUser("Alice", "alice2@example.com", 25));
        
        List<TestUser> found = orm.findByFields(TestUser.class, Map.of(
            "name", "Alice",
            "age", 25
        ));
        Assertions.assertEquals(2, found.size());
    }
    
    @Test
    public void testGetTableName() {
        Orm orm = new MemoryOrm();
        String tableName = orm.getTableName(TestUser.class);
        Assertions.assertEquals("users", tableName);
    }
    
    @Test
    public void testGetIdFieldName() {
        Orm orm = new MemoryOrm();
        String idFieldName = orm.getIdFieldName(TestUser.class);
        Assertions.assertEquals("id", idFieldName);
    }
    
    @Test
    public void testSaveBatch() {
        Orm orm = new MemoryOrm();
        
        List<TestUser> users = Arrays.asList(
            new TestUser("Alice", "alice@example.com", 25),
            new TestUser("Bob", "bob@example.com", 30),
            new TestUser("Charlie", "charlie@example.com", 35)
        );
        
        List<TestUser> saved = orm.saveBatch(users);
        Assertions.assertEquals(3, saved.size());
        
        for (TestUser user : saved) {
            Assertions.assertNotNull(user.getId());
        }
        
        Assertions.assertEquals(3, orm.count(TestUser.class));
    }
    
    @Test
    public void testUpdateBatchById() {
        Orm orm = new MemoryOrm();
        
        TestUser user1 = orm.save(new TestUser("Alice", "alice@example.com", 25));
        TestUser user2 = orm.save(new TestUser("Bob", "bob@example.com", 30));
        
        user1.setName("Alice Updated");
        user2.setName("Bob Updated");
        
        List<TestUser> updated = orm.updateBatchById(Arrays.asList(user1, user2));
        Assertions.assertEquals(2, updated.size());
        
        Optional<TestUser> found1 = orm.find(TestUser.class, user1.getId());
        Optional<TestUser> found2 = orm.find(TestUser.class, user2.getId());
        
        Assertions.assertTrue(found1.isPresent());
        Assertions.assertEquals("Alice Updated", found1.get().getName());
        
        Assertions.assertTrue(found2.isPresent());
        Assertions.assertEquals("Bob Updated", found2.get().getName());
    }
    
    @Test
    public void testRemoveByIds() {
        Orm orm = new MemoryOrm();
        
        TestUser user1 = orm.save(new TestUser("Alice", "alice@example.com", 25));
        TestUser user2 = orm.save(new TestUser("Bob", "bob@example.com", 30));
        TestUser user3 = orm.save(new TestUser("Charlie", "charlie@example.com", 35));
        
        Assertions.assertEquals(3, orm.count(TestUser.class));
        
        int removed = orm.removeByIds(TestUser.class, Arrays.asList(user1.getId(), user2.getId()));
        Assertions.assertEquals(2, removed);
        Assertions.assertEquals(1, orm.count(TestUser.class));
        Assertions.assertTrue(orm.exists(TestUser.class, user3.getId()));
    }
    
    @Test
    public void testPage() {
        Orm orm = new MemoryOrm();
        
        for (int i = 1; i <= 25; i++) {
            orm.save(new TestUser("User " + i, "user" + i + "@example.com", 20 + i));
        }
        
        Page<TestUser> page = new Page<>();
        page.setPageNumber(1);
        page.setPageSize(10);
        
        Page<TestUser> result = orm.page(TestUser.class, page);
        
        Assertions.assertEquals(25, result.getTotal());
        Assertions.assertEquals(10, result.getRecords().size());
        Assertions.assertEquals(1, result.getPageNumber());
        Assertions.assertEquals(10, result.getPageSize());
    }
    
    @Test
    public void testQuery() {
        Orm orm = new MemoryOrm();
        
        orm.save(new TestUser("Alice", "alice@example.com", 25));
        orm.save(new TestUser("Bob", "bob@example.com", 30));
        orm.save(new TestUser("Charlie", "charlie@example.com", 35));
        
        List<TestUser> results = orm.query(TestUser.class)
            .limit(2)
            .get();
        
        Assertions.assertEquals(2, results.size());
    }
}