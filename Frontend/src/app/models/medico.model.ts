export interface Usuario {
  id: number;
  nombre: string;
  cedula: string;
  correo: string;
  contrasena: string;
  fechaNacimiento: string | null;
  rol: string | null;
}

export interface Medico {
  idMedico: number;
  especialidad: string;
  registroProfesional: string;
  user: Usuario;
}