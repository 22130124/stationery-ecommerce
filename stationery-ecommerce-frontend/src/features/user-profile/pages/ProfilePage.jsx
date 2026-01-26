import React, {useEffect, useState} from 'react'
import styles from './ProfilePage.module.scss'
import {getProfile, updateAvatar, updateProfile} from '../../../api/profileApi'
import {token} from '../../../utils/token'
import defaultAvatar from '../assets/default-avatar.png';
import {deleteImage, uploadAvatar} from "../../../api/uploadApi";
import toast from "react-hot-toast";
import {FaEdit, FaCamera} from "react-icons/fa";
import { Spin } from 'antd';

export default function ProfilePage() {
    const [profile, setProfile] = useState({
        fullName: "",
        phone: "",
        address: "",
        avatarUrl: defaultAvatar,
    })
    const [editing, setEditing] = useState(false)
    const [isUploading, setIsUploading] = useState(false)

    useEffect(() => {
        const fetchProfile = async () => {
            const data = await getProfile()
            if (data.avatarUrl) {
                setProfile(data)
            } else {
                setProfile({
                    ...data,
                    avatarUrl: defaultAvatar,
                })
            }

            if (!data.completedStatus) {
                setEditing(true)
                toast('Vui lòng hoàn thiện hồ sơ để không bị gián đoạn trải nghiệm mua hàng', {
                    duration: Infinity,
                    id: 'complete-profile-toast',
                    style: {
                        borderRadius: '12px',
                        background: '#333',
                        color: '#fff',
                        fontSize: '15px',
                    },
                })
            }
        }
        fetchProfile()

        return () => {
            toast.dismiss('complete-profile-toast')
        }
    }, [])

    const handleAvatarChange = async (e) => {
        toast.loading('Đang tải ảnh lên...', { duration: 0 })

        const file = e.target.files[0]
        if (!file) return

        setIsUploading(true)

        setProfile({
            ...profile,
            avatarUrl: URL.createObjectURL(file),
        })

        try {
            if (profile.avatarPublicId) {
                await deleteImage(profile.avatarPublicId)
            }

            const response = await uploadAvatar(file)

            const updatedProfile = await updateAvatar({
                avatarUrl: response.secure_url,
                avatarPublicId: response.public_id,
            })

            setProfile(updatedProfile)
            toast.dismiss()
            toast.success('Cập nhật ảnh đại diện thành công')
        } catch (err) {
            toast.dismiss()
            toast.error('Cập nhật thất bại. Thử lại sau')
        } finally {
            setIsUploading(false)
        }
    }

    const handleSave = async () => {
        toast.dismiss()
        if (!profile.fullName?.trim()) return toast.error('Vui lòng nhập họ và tên')
        if (!profile.phone?.trim()) return toast.error('Vui lòng nhập số điện thoại')
        if (!profile.address?.trim()) return toast.error('Vui lòng nhập địa chỉ')

        toast.loading('Đang lưu thông tin...', {
            id: 'update-info',
            duration: 0,
        });

        try {
            const payload = {
                fullName: profile.fullName,
                phone: profile.phone,
                address: profile.address,
            }

            const data = await updateProfile(payload)
            setProfile(data.avatarUrl ? data : {...data, avatarUrl: defaultAvatar})
            setEditing(false)

            toast.dismiss()
            toast.success('Lưu thành công')
        } catch (err) {

        } finally {
            toast.dismiss('update-info')
        }
    }

    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Hồ sơ cá nhân</h1>

            <div className={styles.card}>
                <div className={styles.avatarWrapper}>
                    <div className={styles.avatarSection}>
                        <img src={profile.avatarUrl} alt="Avatar" className={styles.avatar} />
                        {isUploading && (
                            <div className={styles.overlay}>
                                <Spin size="large" />
                            </div>
                        )}
                        <label className={styles.avatarEdit}>
                            <input type="file" accept="image/*" onChange={handleAvatarChange} />
                            <FaCamera className={styles.cameraIcon} />
                        </label>
                    </div>
                </div>

                <div className={styles.infoSection}>
                    <div className={styles.row}>
                        <label>Email</label>
                        <span className={styles.email}>{token.getEmail()}</span>
                    </div>

                    <div className={styles.row}>
                        <label>Họ và tên <span className={styles.required}>*</span></label>
                        {editing ? (
                            <input
                                type="text"
                                value={profile.fullName}
                                placeholder="Nhập họ và tên đầy đủ"
                                onChange={(e) => setProfile({...profile, fullName: e.target.value})}
                            />
                        ) : (
                            <span>{profile.fullName || 'Chưa cập nhật'}</span>
                        )}
                    </div>

                    <div className={styles.row}>
                        <label>Số điện thoại <span className={styles.required}>*</span></label>
                        {editing ? (
                            <input
                                type="text"
                                value={profile.phone}
                                placeholder="Nhập số điện thoại"
                                onChange={(e) => setProfile({...profile, phone: e.target.value})}
                            />
                        ) : (
                            <span>{profile.phone || 'Chưa cập nhật'}</span>
                        )}
                    </div>

                    <div className={styles.row}>
                        <label>Địa chỉ nhận hàng <span className={styles.required}>*</span></label>
                        {editing ? (
                            <textarea
                                value={profile.address}
                                placeholder="Nhập địa chỉ chi tiết"
                                onChange={(e) => setProfile({...profile, address: e.target.value})}
                                rows={3}
                            />
                        ) : (
                            <span>{profile.address || 'Chưa cập nhật'}</span>
                        )}
                    </div>

                    <div className={styles.actions}>
                        {editing ? (
                            <>
                                <button className={styles.cancelBtn} onClick={() => setEditing(false)}>
                                    Hủy
                                </button>
                                <button className={styles.saveBtn} onClick={handleSave}>
                                    Lưu thay đổi
                                </button>
                            </>
                        ) : (
                            <button className={styles.editBtn} onClick={() => setEditing(true)}>
                                <FaEdit /> Chỉnh sửa
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </div>
    )
}