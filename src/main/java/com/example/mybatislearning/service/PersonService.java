package com.example.mybatislearning.service;

import com.example.mybatislearning.entity.Person;
import com.example.mybatislearning.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

/**
 * PersonService
 * Person管理のビジネスロジックとトランザクション管理
 */
@Service
public class PersonService {

    private final PersonMapper personMapper;

    // メールアドレスの形式検証用パターン
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    /**
     * コンストラクタインジェクション
     *
     * @param personMapper PersonMapper
     */
    @Autowired
    public PersonService(PersonMapper personMapper) {
        this.personMapper = personMapper;
    }

    /**
     * 新しいPersonを作成
     * メールアドレスの形式検証を行う
     *
     * @param person 作成するPerson
     * @throws IllegalArgumentException メールアドレスの形式が不正な場合
     */
    @Transactional
    public void createPerson(Person person) {
        validateEmail(person.getEmail());
        personMapper.insert(person);
    }

    /**
     * IDでPersonを取得
     *
     * @param id Person ID
     * @return 該当するPerson、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Person findById(Long id) {
        return personMapper.selectById(id);
    }

    /**
     * すべてのPersonを取得
     *
     * @return Personのリスト
     */
    @Transactional(readOnly = true)
    public List<Person> findAll() {
        return personMapper.selectAll();
    }

    /**
     * メールアドレスでPersonを検索
     *
     * @param email メールアドレス
     * @return 該当するPerson、存在しない場合はnull
     */
    @Transactional(readOnly = true)
    public Person findByEmail(String email) {
        return personMapper.findByEmail(email);
    }

    /**
     * Personを更新
     * メールアドレスの形式検証を行う
     *
     * @param person 更新するPerson
     * @throws IllegalArgumentException メールアドレスの形式が不正な場合
     */
    @Transactional
    public void updatePerson(Person person) {
        validateEmail(person.getEmail());
        personMapper.update(person);
    }

    /**
     * Personを削除
     *
     * @param id 削除するPerson ID
     */
    @Transactional
    public void deletePerson(Long id) {
        personMapper.deleteById(id);
    }

    /**
     * メールアドレスの形式を検証
     * nullの場合は検証をスキップ（nullは許容）
     *
     * @param email 検証するメールアドレス
     * @throws IllegalArgumentException メールアドレスの形式が不正な場合
     */
    private void validateEmail(String email) {
        if (email == null) {
            return; // nullは許容
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(
                    "無効なメールアドレス形式です: " + email
            );
        }
    }
}
