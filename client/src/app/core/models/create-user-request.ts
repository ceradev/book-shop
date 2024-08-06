export interface CreateUserRequest {
    username: string;
    password: string;
    name: string;
    surname: string;
    email: string;
    roles: string[];
}