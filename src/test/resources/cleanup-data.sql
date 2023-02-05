delete from membership;
delete from role;

insert into role(id, name) values ('1b3c333b-36e7-4b64-aa15-c22ed5908ce4', 'Developer');
insert into role(id, name) values ('25bbb7d2-26f3-11ec-9621-0242ac130002', 'Product Owner');
insert into role(id, name) values ('37969e22-26f3-11ec-9621-0242ac130002', 'Tester');

insert into membership(id, role_id, team_id, user_id) values (
    '98de61a0-b9e3-11ec-8422-0242ac120002',
    '1b3c333b-36e7-4b64-aa15-c22ed5908ce4',
    '7676a4bf-adfe-415c-941b-1739af07039b',
    'fd282131-d8aa-4819-b0c8-d9e0bfb1b75c'
);
