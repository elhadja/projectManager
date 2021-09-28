export interface UpdateProjectOutputDTO {
    projectName: string,
    projectDescription?: string,
    projectManagersIds: number[],
    projectUsersIds: number[]
}