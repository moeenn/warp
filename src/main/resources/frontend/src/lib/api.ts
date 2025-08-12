const BASE_URL = "http://localhost:8080/api"

type LocalIPResponse = {
    ip: string
}

export async function getLocalIP(): Promise<LocalIPResponse> {
    const url = BASE_URL + "/local-ip"
    const res = await fetch(url)
    return await res.json() as LocalIPResponse
}

type ListFilesResponse = {
    files: string[]
}

export async function listFiles(): Promise<ListFilesResponse> {
    const url = BASE_URL + "/files"
    const res = await fetch(url)
    return await res.json() as ListFilesResponse
}