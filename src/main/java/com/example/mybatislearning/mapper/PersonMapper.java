package com.example.mybatislearning.mapper;

import com.example.mybatislearning.entity.Person;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * PersonMapper（XML）インターフェース
 * XMLマッパーファイルでSQL定義を行うCRUD操作
 */
@Mapper
public interface PersonMapper {

    /**
     * 新しいPersonを挿入
     *
     * @param person 挿入するPerson（IDは自動採番）
     */
    void insert(Person person);

    /**
     * IDでPersonを検索
     *
     * @param id 検索するPerson ID
     * @return 該当するPerson、存在しない場合はnull
     */
    Person selectById(Long id);

    /**
     * すべてのPersonを取得
     *
     * @return Personのリスト
     */
    List<Person> selectAll();

    /**
     * メールアドレスでPersonを検索
     *
     * @param email 検索するメールアドレス
     * @return 該当するPerson、存在しない場合はnull
     */
    Person findByEmail(String email);

    /**
     * Personを更新
     *
     * @param person 更新するPerson
     */
    void update(Person person);

    /**
     * IDでPersonを削除
     *
     * @param id 削除するPerson ID
     */
    void deleteById(Long id);
}
