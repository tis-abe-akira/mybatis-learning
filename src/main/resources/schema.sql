-- organizationsテーブルの作成
CREATE TABLE IF NOT EXISTS organizations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

-- projectsテーブルの作成
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    organization_id BIGINT NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES organizations(id)
);
