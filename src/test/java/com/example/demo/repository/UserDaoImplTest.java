package com.example.demo.repository;

import com.example.demo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("unit")
@Sql
class UserDaoImplTest {

    @Autowired
    private UserDaoImpl userDao;

    @Test
    @DisplayName("findAllのテスト")
    void findAll() {
        var list = userDao.findAll();

        // 件数のチェック
        assertEquals(4, list.size());

        // 2件目のレコードの取得(ORDER BYが正しく反映されているか)
        var user2 = list.get(1);
        assertNotNull(user2);

        // 各カラムの値が正しくセットされているか
        assertEquals("ユーザー2", user2.getUsername());
        assertEquals("user2@example.com", user2.getEmail());
        assertEquals("pass2", user2.getPassword());
        assertFalse(user2.isEnabled());
        assertEquals("USER", user2.getAuthorityId());
        assertEquals("key2", user2.getTempkey());

        var user3 = list.get(2);
        assertNotNull(user3);

        assertEquals("ユーザー3", user3.getUsername());

        // enabledがtrueの場合に正しくセットされているか
        assertTrue(user3.isEnabled());
    }

    @Test
    @DisplayName("findActiveUsersのテスト")
    void findActiveUsers() {
        var list = userDao.findActiveUsers();
        // 件数のチェック
        assertEquals(3, list.size());

        // 3件目のレコードの取得(ORDER BYが正しく反映されているか)
        var user4 = list.get(2);
        assertNotNull(user4);

        // 各カラムの値が正しくセットされているか
        assertEquals("ユーザー4", user4.getUsername());
        assertEquals("user4@example.com", user4.getEmail());
        assertEquals("pass4", user4.getPassword());
        assertTrue(user4.isEnabled());
        assertEquals("ADMIN", user4.getAuthorityId());
        assertEquals("key4", user4.getTempkey());

        // 全てのレコードがenabled = trueかチェック
        assertTrue(list.stream().allMatch(User::isEnabled));
    }

    @Test
    @DisplayName("findByIdのテスト(正常系)")
    void findById1() {
        var user1 = userDao.findById(1);

        // レコードの存在チェック
        assertNotNull(user1);

        // 各カラムの値が正しくセットされているか
        assertEquals("ユーザー1", user1.getUsername());
        assertEquals("user1@example.com", user1.getEmail());
        assertEquals("pass1", user1.getPassword());
        assertTrue(user1.isEnabled());
        assertEquals("USER", user1.getAuthorityId());
        assertEquals("key1", user1.getTempkey());
    }

    @Test
    @DisplayName("findByIdのテスト(レコードが取得できない場合)")
    void findById2() {
        // レコードが取得できず例外がスローされるか
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById(10));
    }

    @Test
    @DisplayName("insertのテスト(正常系)")
    void insert() {
        var user = new User();
        user.setUsername("ユーザーX");
        user.setEmail("userx@example.com");
        user.setPassword("passx");
        user.setEnabled(true);
        user.setAuthorityId("USER");
        user.setTempkey("keyx");

        var insertCount = userDao.insert(user);

        assertEquals(1, insertCount);

        var list = userDao.findAll();

        // 件数のチェック
        assertEquals(5, list.size());

        // 登録されたレコードの取得
        var userx = list.get(4);

        // 各カラムの値が正しくセットされているか
        assertEquals(user.getUsername(), userx.getUsername());
        assertEquals(user.getEmail(), userx.getEmail());
        assertEquals(user.getPassword(), userx.getPassword());
        assertEquals(user.isEnabled(), userx.isEnabled());
        assertEquals(user.getAuthorityId(), userx.getAuthorityId());
        assertEquals(user.getTempkey(), userx.getTempkey());
    }

    @Test
    @DisplayName("updateのテスト(正常系)")
    void update1() {
        var user = new User();
        user.setId(2);
        user.setUsername("ユーザー2(NEW)");
        user.setEmail("user2-new@example.com");
        user.setPassword("pass2_new");
        user.setEnabled(false);
        user.setAuthorityId("ADMIN");
        user.setTempkey("key2_new");

        var updateCount = userDao.update(user);

        assertEquals(1, updateCount);

        var user2 = userDao.findById(2);

        // レコードの存在チェック
        assertNotNull(user2);

        // 各カラムの値が正しくセットされているか
        assertEquals(user.getUsername(), user2.getUsername());
        assertEquals(user.getEmail(), user2.getEmail());
        assertEquals(user.getPassword(), user2.getPassword());
        assertEquals(user.isEnabled(), user2.isEnabled());
        assertEquals(user.getAuthorityId(), user2.getAuthorityId());
        assertEquals(user.getTempkey(), user2.getTempkey());
    }

    @Test
    @DisplayName("updateのテスト(更新対象がない場合)")
    void update2() {
        var user = new User();
        user.setId(10);
        var updateCount = userDao.update(user);
        assertEquals(0, updateCount);
    }

    @Test
    @DisplayName("deleteByIdのテスト(正常系)")
    void deleteById1() {
        userDao.deleteById(1);

        var list = userDao.findAll();

        // 件数のチェック(対象外のレコードまで消えていないかチェック)
        assertEquals(3, list.size());

        // レコードが取得できないことを確認
        assertThrows(EmptyResultDataAccessException.class, () -> userDao.findById(1));
    }

    @Test
    @DisplayName("deleteByIdのテスト(更新対象がない場合)")
    void deleteById2() {
        var deleteCount = userDao.deleteById(10);
        assertEquals(0, deleteCount);

        var list = userDao.findAll();

        // 件数のチェック(全てのレコードが消えていない事を確認)
        assertEquals(4, list.size());
    }
}
