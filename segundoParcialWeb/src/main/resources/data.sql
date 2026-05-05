insert into roles (nombre)
select 'ADMINISTRADOR' where not exists (select 1 from roles where nombre = 'ADMINISTRADOR');
insert into roles (nombre)
select 'MEDICO' where not exists (select 1 from roles where nombre = 'MEDICO');
insert into roles (nombre)
select 'PACIENTE' where not exists (select 1 from roles where nombre = 'PACIENTE');

insert into usuarios (username, password, enabled)
select 'admin', '{noop}admin123', true where not exists (select 1 from usuarios where username = 'admin');
insert into usuarios (username, password, enabled)
select 'medico1', '{noop}medico123', true where not exists (select 1 from usuarios where username = 'medico1');
insert into usuarios (username, password, enabled)
select 'paciente1', '{noop}paciente123', true where not exists (select 1 from usuarios where username = 'paciente1');
insert into usuarios (username, password, enabled)
select 'paciente2', '{noop}paciente123', true where not exists (select 1 from usuarios where username = 'paciente2');

insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u, roles r
where u.username = 'admin' and r.nombre = 'ADMINISTRADOR'
  and not exists (select 1 from usuario_roles ur where ur.usuario_id = u.id and ur.rol_id = r.id);
insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u, roles r
where u.username = 'medico1' and r.nombre = 'MEDICO'
  and not exists (select 1 from usuario_roles ur where ur.usuario_id = u.id and ur.rol_id = r.id);
insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u, roles r
where u.username = 'paciente1' and r.nombre = 'PACIENTE'
  and not exists (select 1 from usuario_roles ur where ur.usuario_id = u.id and ur.rol_id = r.id);
insert into usuario_roles (usuario_id, rol_id)
select u.id, r.id from usuarios u, roles r
where u.username = 'paciente2' and r.nombre = 'PACIENTE'
  and not exists (select 1 from usuario_roles ur where ur.usuario_id = u.id and ur.rol_id = r.id);

insert into medicos (nombre, especialidad, documento, usuario_id)
select 'Laura Perez', 'Medicina General', '1002003001', u.id from usuarios u
where u.username = 'medico1' and not exists (select 1 from medicos where documento = '1002003001');

insert into pacientes (nombre, documento, usuario_id)
select 'Ana Gomez', '1100110011', u.id from usuarios u
where u.username = 'paciente1' and not exists (select 1 from pacientes where documento = '1100110011');
insert into pacientes (nombre, documento, usuario_id)
select 'Carlos Ruiz', '2200220022', u.id from usuarios u
where u.username = 'paciente2' and not exists (select 1 from pacientes where documento = '2200220022');

insert into consultorios (numero)
select 101 where not exists (select 1 from consultorios where numero = 101);
insert into consultorios (numero)
select 102 where not exists (select 1 from consultorios where numero = 102);
