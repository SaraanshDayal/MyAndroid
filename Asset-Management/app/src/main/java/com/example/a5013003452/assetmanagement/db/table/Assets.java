package com.example.a5013003452.assetmanagement.db.table;

/**
 * Created by 5013003452 on 3/6/2017.
 */

public class Assets
{
    private String AssetID;
    private String Sno;
    private String Desc;
    private String ImgPath;
    private String Owner;
    private String status;
    private Long OwnTagData;
    private Long AlloTagData;
    private String Allotte;
    private String Category;

    public Assets()
    {
    }
    public Assets(String AssetID,String Sno,String Desc,String ImgPath,String Owner,String status,Long OwnTagData,Long AlloTagData, String Allotte,String Category)
    {
        this.AssetID=AssetID;
        this.Sno=Sno;
        this.Desc=Desc;
        this.ImgPath=ImgPath;
        this.Owner=Owner;
        this.status=status;
        this.OwnTagData=OwnTagData;
        this.AlloTagData=AlloTagData;
        this.Allotte=Allotte;
        this.Category=Category;
    }
    public void setAssetID(String AssetID)
    {
        this.AssetID=AssetID;
    }
    public void setSno(String Sno)
    {
        this.Sno = Sno;
    }
    public void setDesc(String Desc)
    {
        this.Desc = Desc;
    }
    public void setImgPath(String ImgPath)
    {
        this.ImgPath = ImgPath;
    }
    public void setOwner(String Owner)
    {
        this.Owner = Owner;
    }
    public void setstatus(String status)
    {
        this.status = status;
    }
    public void setOwnTagData(Long TagData)
    {
        this.OwnTagData = OwnTagData;
    }
    public void setAlloTagData(Long TagData)
    {
        this.AlloTagData = AlloTagData;
    }
    public void setAllotte(String Allotee)  { this.Allotte=Allotee; }
    public void setCategory(String Category)  { this.Category=Category; }


    public String getAssetID()
    {
        return AssetID;
    }
    public String getSno()
    {
        return Sno;
    }
    public String getDesc()
    {
        return Desc;
    }
    public String getImgPath()
    {
        return ImgPath;
    }
    public String getOwner()
    {
        return Owner;
    }
    public String getstatus()
    {
        return status;
    }
    public Long getOwmTagData()
    {
        return OwnTagData;
    }
    public Long getAlloTagData()
    {
        return AlloTagData;
    }
    public String getAllotte() { return Allotte; }
    public String getCategory() { return Category; }
};
