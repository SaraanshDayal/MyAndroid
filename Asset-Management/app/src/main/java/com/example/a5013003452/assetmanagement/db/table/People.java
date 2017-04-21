package com.example.a5013003452.assetmanagement.db.table;

/**
 * Created by 5013003452 on 2/8/2017.
 */
public class People
{
    private long GID;
    private long TagId;
    private String EmpName;
    private String ImgPath;

    public People()
    {
    }
    public People(long GID,long TagId,String EmpName,String ImgPath)
    {
        this.GID=GID;
        this.TagId=TagId;
        this.EmpName=EmpName;
        this.ImgPath=ImgPath;
    }
    public void setGID(long GID)
    {
        this.GID=GID;
    }
    public void setTagId(long TagId)
    {
        this.TagId = TagId;
    }

    public void setEmp(String EmpName)
    {
        this.EmpName = EmpName;
    }
    public void setImgPath(String ImgPath)
    {
        this.ImgPath = ImgPath;
    }
    public long getGID()
    {
        return GID;
    }
    public long getTagId()
    {
        return TagId;
    }
    public String getEmp()
    {
        return EmpName;
    }
    public String getImgPath()
    {
        return ImgPath;
    }
};