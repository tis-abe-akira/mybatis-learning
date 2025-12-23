-- organizationsテーブルの作成
CREATE TABLE IF NOT EXISTS organizations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500)
);

-- personsテーブルの作成（タスク1.1）
CREATE TABLE IF NOT EXISTS persons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role VARCHAR(100),
    department VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- personsテーブルのインデックス
CREATE INDEX IF NOT EXISTS idx_persons_email ON persons(email);

-- industriesテーブルの作成（タスク1.1）
CREATE TABLE IF NOT EXISTS industries (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- industriesテーブルのインデックス
CREATE INDEX IF NOT EXISTS idx_industries_name ON industries(name);

-- technologiesテーブルの作成（タスク1.1）
CREATE TABLE IF NOT EXISTS technologies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- technologiesテーブルのインデックス
CREATE INDEX IF NOT EXISTS idx_technologies_category ON technologies(category);

-- projectsテーブルの作成（タスク1.2で拡張）
CREATE TABLE IF NOT EXISTS projects (
    -- 既存フィールド
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    organization_id BIGINT NOT NULL,

    -- 新規フィールド（顧客情報）
    customer_name VARCHAR(255),
    industry_id BIGINT,

    -- 新規フィールド（プロジェクト特性）
    project_type VARCHAR(50),
    status VARCHAR(50),

    -- 新規フィールド（規模）
    budget DECIMAL(15, 2) CHECK (budget >= 0),
    person_months DECIMAL(10, 2),
    team_size INTEGER,

    -- 新規フィールド（期間）
    planned_start_date DATE,
    planned_end_date DATE,
    actual_start_date DATE,
    actual_end_date DATE,

    -- 新規フィールド（責任者）
    project_manager_id BIGINT,
    technical_lead_id BIGINT,

    -- 外部キー制約
    FOREIGN KEY (organization_id) REFERENCES organizations(id),
    FOREIGN KEY (industry_id) REFERENCES industries(id),
    FOREIGN KEY (project_manager_id) REFERENCES persons(id),
    FOREIGN KEY (technical_lead_id) REFERENCES persons(id)
);

-- projectsテーブルの拡張インデックス（タスク1.2）
CREATE INDEX IF NOT EXISTS idx_projects_industry_id ON projects(industry_id);
CREATE INDEX IF NOT EXISTS idx_projects_status ON projects(status);
CREATE INDEX IF NOT EXISTS idx_projects_project_type ON projects(project_type);
CREATE INDEX IF NOT EXISTS idx_projects_project_manager_id ON projects(project_manager_id);
CREATE INDEX IF NOT EXISTS idx_projects_technical_lead_id ON projects(technical_lead_id);

-- phasesテーブルの作成（タスク1.1）
CREATE TABLE IF NOT EXISTS phases (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    phase_type VARCHAR(50) NOT NULL,
    planned_start_date DATE NOT NULL,
    planned_end_date DATE NOT NULL,
    actual_start_date DATE,
    actual_end_date DATE,
    status VARCHAR(50) DEFAULT 'NOT_STARTED',
    deliverables TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

-- phasesテーブルのインデックス
CREATE INDEX IF NOT EXISTS idx_phases_project_id ON phases(project_id);

-- project_membersテーブルの作成（タスク1.1）
CREATE TABLE IF NOT EXISTS project_members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    person_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    join_date DATE NOT NULL,
    leave_date DATE,
    allocation_rate DECIMAL(3, 2) NOT NULL CHECK (allocation_rate >= 0.0 AND allocation_rate <= 1.0),
    activities TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (person_id) REFERENCES persons(id)
);

-- project_membersテーブルのインデックス
CREATE INDEX IF NOT EXISTS idx_project_members_project_id ON project_members(project_id);
CREATE INDEX IF NOT EXISTS idx_project_members_person_id ON project_members(person_id);

-- project_technologiesテーブルの作成（タスク1.1）
CREATE TABLE IF NOT EXISTS project_technologies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    technology_id BIGINT NOT NULL,
    purpose VARCHAR(500) NOT NULL,
    version VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    FOREIGN KEY (technology_id) REFERENCES technologies(id)
);

-- project_technologiesテーブルのインデックス
CREATE INDEX IF NOT EXISTS idx_project_technologies_project_id ON project_technologies(project_id);
CREATE INDEX IF NOT EXISTS idx_project_technologies_technology_id ON project_technologies(technology_id);
