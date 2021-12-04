export interface GetTaskInputDTO {
    id?: number,
    userStoryId: number,
    description: string,
    duration?: number,
    definitionOfDone?: string
    userId: number
}