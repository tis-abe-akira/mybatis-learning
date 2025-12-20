-- organizationsテーブルのテストデータ
INSERT INTO organizations (name, description) VALUES
    ('Tech Corp', 'Technology company focused on software development'),
    ('Business Solutions Inc', 'Enterprise business consulting services');

-- projectsテーブルのテストデータ
INSERT INTO projects (project_name, organization_id) VALUES
    ('Web Application Redesign', 1),
    ('Mobile App Development', 1),
    ('ERP System Integration', 2);
