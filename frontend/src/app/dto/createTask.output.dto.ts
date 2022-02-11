export interface CreateTaskOutputDTO {
    userId?: number,
    userStoriesIDs: number,
    description: string,
    duration?: number,
    definitionOfDone?: string,
    dependenciesIDs?: number[]
}