import React, {useEffect, useState} from 'react'
import styles from './ProfilePage.module.scss'
import {getProfile, updateAvatar, updateProfile} from '../../../api/profileApi'
import {token} from '../../../utils/token'
import defaultAvatar from '../assets/default-avatar.png';
import {deleteImage, uploadAvatar, uploadImage} from "../../../api/uploadApi";
import toast from "react-hot-toast";
import {FaEdit} from "react-icons/fa";
import { Spin } from 'antd';

export default function ProfilePage() {
    const [profile, setProfile] = useState({
        fullName: "",
        phone: "",
        address: "",
        avatarUrl: '../assets/default-avatar.png',
    })
    const [editing, setEditing] = useState(false)
    const [isUploading, setIsUploading] = useState(false)

    // Hàm fetch profile của người dùng hiện tại
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

            // Kiểm tra xem thông tin người dùng đã đầy đủ hay chưa
            let toastId
            if (!data.completedStatus) {
                setEditing(true)
                toast.dismiss()
                toastId = toast('Vui lòng hoàn thiện hồ sơ để không bị gián đoạn trải nghiệm mua hàng', {
                    duration: Infinity,
                    id: 'complete-profile-toast',
                })
            }
        }
        fetchProfile()

        return () => {
            // khi rời khỏi ProfilePage
            toast.dismiss('complete-profile-toast')
        }
    }, [])
    console.log("Profile", profile)

    // Hàm xử lý upload ảnh đại diện
    const handleAvatarChange = async (e) => {
        toast.loading('Đang tải ảnh lên...')

        const file = e.target.files[0]
        if (!file) return

        setIsUploading(true)

        // preview trước
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
            toast.error('Cập nhật ảnh đại diện thất bại. Vui lòng thử lại sau')
        } finally {
            setIsUploading(false)
        }
    }

    const handleSave = async () => {
        toast.dismiss()
        if (!profile.fullName?.trim()) {
            toast.error('Vui lòng nhập họ và tên')
            return
        }
        if (!profile.phone?.trim()) {
            toast.error('Vui lòng nhập số điện thoại')
            return
        }
        if (!profile.address?.trim()) {
            toast.error('Vui lòng nhập địa chỉ')
            return
        }

        toast.loading('Đang cập nhật thông tin...')

        try {
            const payload = {
                fullName: profile.fullName,
                phone: profile.phone,
                address: profile.address,
            }

            const data = await updateProfile(payload)

            if (data.avatarUrl) {
                setProfile(data)
            } else {
                setProfile({
                    ...data,
                    avatarUrl: defaultAvatar,
                })
            }
            setEditing(false)

            toast.dismiss()
            toast.success('Cập nhật thông tin thành công')
        } catch (err) {
            toast.dismiss()
            toast.error('Cập nhật thông tin thất bại. Vui lòng thử lại sau')
        }
    }

    return (
        profile && (
            <div className={styles.container}>
                <h1 className={styles.title}>Hồ sơ cá nhân</h1>

                <div className={styles.card}>
                    <div className={styles.avatarSection}>
                        {profile.avatarUrl && (
                            <img src={profile.avatarUrl} className={styles.avatar}/>
                        )}
                        {isUploading && (
                            <div className={styles.overlay}>
                                <Spin size="large" />
                            </div>
                        )}
                        <label className={styles.avatarEdit}>
                            <input
                                type='file'
                                accept='image/*'
                                onChange={handleAvatarChange}
                            />
                            <FaEdit />
                        </label>
                    </div>

                    <div className={styles.row}>
                        <label>Email *</label>
                        <span>{token.getEmail()}</span>
                    </div>

                    <div className={styles.row}>
                        <label>Họ và tên *</label>
                        {editing ? (
                            <input
                                type='text'
                                value={profile.fullName}
                                placeholder={'Nhập họ và tên đầy đủ của bạn'}
                                onChange={(e) =>
                                    setProfile({...profile, fullName: e.target.value})
                                }
                            />
                        ) : (
                            <span>{profile.fullName}</span>
                        )}
                    </div>

                    <div className={styles.row}>
                        <label>Số điện thoại *</label>
                        {editing ? (
                            <input
                                type='text'
                                value={profile.phone}
                                placeholder={'Nhập số điện thoại của bạn'}
                                onChange={(e) =>
                                    setProfile({...profile, phone: e.target.value})
                                }
                            />
                        ) : (
                            <span>{profile.phone}</span>
                        )}
                    </div>

                    <div className={styles.row}>
                        <label>Địa chỉ *</label>
                        {editing ? (
                            <textarea
                                value={profile.address}
                                placeholder={'Nhập địa chỉ nhận hàng'}
                                onChange={(e) =>
                                    setProfile({...profile, address: e.target.value})
                                }
                            />
                        ) : (
                            <span>{profile.address}</span>
                        )}
                    </div>

                    <div className={styles.actions}>
                        {editing ? (
                            <button className={styles.saveBtn} onClick={handleSave}>
                                Lưu
                            </button>
                        ) : (
                            <button
                                className={styles.editBtn}
                                onClick={() => setEditing(true)}
                            >
                                Chỉnh sửa
                            </button>
                        )}
                    </div>
                </div>
            </div>
        )
    )
}